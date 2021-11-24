package com.github.aws404.polyguitest.furnace;

import com.github.aws404.polyguitest.ModInit;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class TriFurnaceBlockEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeInputProvider, RecipeUnlocker {

    public static int FUEL_SLOT = 0;
    public static int[] INPUT_SLOTS = {1, 3, 5};
    public static int[] OUTPUT_SLOTS = {2, 4, 6};

    protected DefaultedList<ItemStack> fuel = DefaultedList.ofSize(1, ItemStack.EMPTY);
    protected DefaultedList<ItemStack> first = DefaultedList.ofSize(2, ItemStack.EMPTY);
    protected DefaultedList<ItemStack> second = DefaultedList.ofSize(2, ItemStack.EMPTY);
    protected DefaultedList<ItemStack> third = DefaultedList.ofSize(2, ItemStack.EMPTY);
    protected List<DefaultedList<ItemStack>> combinedInventory = ImmutableList.of(fuel, first, second, third);
    protected int burnTime;
    protected int fuelTime;
    protected int[] cookTimes = {0, 0, 0};
    protected int[] cookTimeTotals = {0, 0, 0};

    private final Object2IntOpenHashMap<Identifier>[] recipesUsed;

    public TriFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModInit.TRI_FURNACE_BLOCK_ENTITY, blockPos, blockState);
        this.recipesUsed = new Object2IntOpenHashMap[]{
                new Object2IntOpenHashMap<Identifier>(),
                new Object2IntOpenHashMap<Identifier>(),
                new Object2IntOpenHashMap<Identifier>()
        };

    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.tri_furnace");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return switch (side) {
            case DOWN -> OUTPUT_SLOTS;
            case UP -> INPUT_SLOTS;
            default -> new int[]{FUEL_SLOT};
        };
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (ArrayUtils.contains(OUTPUT_SLOTS, slot)) {
            return false;
        } else if (ArrayUtils.contains(INPUT_SLOTS, slot)) {
            return true;
        } else {
            ItemStack itemStack = this.getStack(FUEL_SLOT);
            return AbstractFurnaceBlockEntity.canUseAsFuel(stack) || stack.isOf(Items.BUCKET) && !itemStack.isOf(Items.BUCKET);
        }
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void dropExperienceForRecipesUsed(ServerPlayerEntity player, int slot) {
        List<Recipe<?>> list = this.getRecipesUsedAndDropExperience(slot, player.getWorld(), player.getPos());
        player.unlockRecipes(list);
        this.recipesUsed[slot].clear();
    }

    public List<Recipe<?>> getRecipesUsedAndDropExperience(int slot, ServerWorld world, Vec3d pos) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<Identifier> identifierEntry : this.recipesUsed[slot].object2IntEntrySet()) {
            world.getRecipeManager().get(identifierEntry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                dropExperience(world, pos, identifierEntry.getIntValue(), ((AbstractCookingRecipe) recipe).getExperience());
            });
        }

        return list;
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
        int i = MathHelper.floor((float)multiplier * experience);
        float f = MathHelper.fractionalPart((float)multiplier * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrbEntity.spawn(world, pos, i);
    }


    private boolean hasSomethingToTryAndSmelt() {
        for (int inputSlot : INPUT_SLOTS) {
            if (!this.getStack(inputSlot).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, TriFurnaceBlockEntity blockEntity) {
        boolean wasBurning = blockEntity.isBurning();
        boolean bl2 = false;
        if (blockEntity.isBurning()) {
            --blockEntity.burnTime;
        }

        ItemStack fuel = blockEntity.getStack(FUEL_SLOT);
        // Reduce progress if there is no fuel left
        if (!blockEntity.isBurning() && (fuel.isEmpty() || !blockEntity.hasSomethingToTryAndSmelt())) {
            for (int i = 0; i < INPUT_SLOTS.length; i++) {
                int cookTime = blockEntity.cookTimes[i];
                if (blockEntity.isBurning() && cookTime > 0) {
                    blockEntity.cookTimes[i] = MathHelper.clamp(cookTime - 2, 0, blockEntity.cookTimeTotals[i]);
                }
            }
        } else {
            int maxCountPerStack = blockEntity.getMaxCountPerStack();
            for (int i = 0; i < INPUT_SLOTS.length; i++) {
                DefaultedList<ItemStack> inventory = blockEntity.combinedInventory.get(i + 1);
                Inventory inv = new SimpleInventory(inventory.toArray(ItemStack[]::new));
                Recipe<?> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, inv, world).orElse(null);

                if (recipe == null) {
                    continue;
                }

                if (!blockEntity.isBurning() && canAcceptRecipeOutput(recipe, inventory, maxCountPerStack)) {
                    blockEntity.burnTime = blockEntity.getFuelTime(fuel) / 3;
                    blockEntity.fuelTime = blockEntity.burnTime;
                    if (blockEntity.isBurning()) {
                        bl2 = true;
                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.decrement(1);
                            if (fuel.isEmpty()) {
                                Item item2 = item.getRecipeRemainder();
                                blockEntity.setStack(FUEL_SLOT, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                            }
                        }
                    }
                }

                if (blockEntity.isBurning() && canAcceptRecipeOutput(recipe, inventory, maxCountPerStack)) {
                    blockEntity.cookTimes[i] = ++blockEntity.cookTimes[i];
                    if (blockEntity.cookTimes[i] == blockEntity.cookTimeTotals[i]) {
                        blockEntity.cookTimes[i] = 0;
                        blockEntity.cookTimeTotals[i] = getCookTime(world, blockEntity);
                        if (craftRecipe(recipe, blockEntity.fuel, inventory, maxCountPerStack)) {
                            blockEntity.setLastRecipe(recipe, i);
                        }

                        bl2 = true;
                    }
                } else {
                    blockEntity.cookTimes[i] = 0;
                }
            }

        }

        if (wasBurning != blockEntity.isBurning()) {
            bl2 = true;
          //  state = state.with(AbstractFurnaceBlock.LIT, blockEntity.isBurning());
           // world.setBlockState(pos, state, 3);
        }

        if (bl2) {
            markDirty(world, pos, state);
        }

    }

    private static boolean craftRecipe(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> fuel, DefaultedList<ItemStack> slots, int count) {
        if (recipe != null && canAcceptRecipeOutput(recipe, slots, count)) {
            ItemStack input = slots.get(0);
            ItemStack outputToAdd = recipe.getOutput();
            ItemStack currentOutput = slots.get(1);
            if (currentOutput.isEmpty()) {
                slots.set(1, outputToAdd.copy());
            } else if (currentOutput.isOf(outputToAdd.getItem())) {
                currentOutput.increment(1);
            }

            if (input.isOf(Blocks.WET_SPONGE.asItem()) && !fuel.get(0).isEmpty() && fuel.get(0).isOf(Items.BUCKET)) {
                fuel.set(0, new ItemStack(Items.WATER_BUCKET));
            }

            input.decrement(1);
            return true;
        } else {
            return false;
        }
    }

    public void setLastRecipe(@Nullable Recipe<?> recipe, int slot) {
        if (recipe != null) {
            Identifier identifier = recipe.getId();
            this.recipesUsed[slot].addTo(identifier, 1);
        }

    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(item, 0);
        }
    }

    private static boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count) {
        if (!slots.get(0).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.getOutput();
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = slots.get(1);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!itemStack2.isItemEqualIgnoreDamage(itemStack)) {
                    return false;
                } else if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
                    return true;
                } else {
                    return itemStack2.getCount() < itemStack.getMaxCount();
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == FUEL_SLOT) {
            return stack.isOf(Items.WATER_BUCKET) || stack.isOf(Items.BUCKET);
        } else {
            return true;
        }
    }

    @Override
    public int size() {
        int size = 0;
        for (DefaultedList<ItemStack> itemStacks : this.combinedInventory) {
            size += itemStacks.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (DefaultedList<ItemStack> itemStacks : this.combinedInventory) {
            if (!itemStacks.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        DefaultedList<ItemStack> selectedList = null;

        DefaultedList<ItemStack> currentList;
        for(Iterator<DefaultedList<ItemStack>> var4 = this.combinedInventory.iterator(); var4.hasNext(); slot -= currentList.size()) {
            currentList = var4.next();
            if (slot < currentList.size()) {
                selectedList = currentList;
                break;
            }
        }

        if (selectedList != null) {
            return selectedList.get(slot);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        List<ItemStack> list = null;

        DefaultedList<ItemStack>  defaultedList;
        for(Iterator<DefaultedList<ItemStack> > var4 = this.combinedInventory.iterator(); var4.hasNext(); slot -= defaultedList.size()) {
            defaultedList = var4.next();
            if (slot < defaultedList.size()) {
                list = defaultedList;
                break;
            }
        }

        return list != null && !list.get(slot).isEmpty() ? Inventories.splitStack(list, slot, amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        DefaultedList<ItemStack> selectedInventory = null;

        DefaultedList<ItemStack> current;
        for(Iterator<DefaultedList<ItemStack>> var3 = this.combinedInventory.iterator(); var3.hasNext(); slot -= current.size()) {
            current = var3.next();
            if (slot < current.size()) {
                selectedInventory = current;
                break;
            }
        }

        if (selectedInventory != null && !selectedInventory.get(slot).isEmpty()) {
            ItemStack itemStack = selectedInventory.get(slot);
            selectedInventory.set(slot, ItemStack.EMPTY);
            return itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        DefaultedList<ItemStack> selectedInventory = null;

        DefaultedList<ItemStack> currentInventory;
        int selected = -2;
        for(Iterator<DefaultedList<ItemStack>> var3 = this.combinedInventory.iterator(); var3.hasNext(); slot -= currentInventory.size()) {
            currentInventory = var3.next();
            selected++;
            if (slot < currentInventory.size()) {
                selectedInventory = currentInventory;
                break;
            }
        }

        if (selectedInventory == null) {
            return;
        }

        ItemStack itemStack = selectedInventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areNbtEqual(stack, itemStack);
        selectedInventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        if (slot == 0 && !bl && selected >= 0) {
            this.cookTimeTotals[selected] = getCookTime(this.world, new SimpleInventory(selectedInventory.toArray(ItemStack[]::new)));
            this.cookTimes[selected] = 0;
            this.markDirty();
        }
    }

    private static int getCookTime(World world, Inventory inventory) {
        return world.getRecipeManager().getFirstMatch((RecipeType<? extends AbstractCookingRecipe>) RecipeType.SMELTING, inventory, world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        for (DefaultedList<ItemStack> itemStacks : this.combinedInventory) {
            itemStacks.clear();
        }
    }

    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for (DefaultedList<ItemStack> itemStacks : this.combinedInventory) {
            for (ItemStack itemStack : itemStacks) {
                finder.addInput(itemStack);
            }
        }
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        this.setLastRecipe(recipe, 0);
    }

    @Nullable
    @Override
    public Recipe<?> getLastRecipe() {
        return null;
    }

    @Override
    public void unlockLastRecipe(PlayerEntity player) {
    }
}
