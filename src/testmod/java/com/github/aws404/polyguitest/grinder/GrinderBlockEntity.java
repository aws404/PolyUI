package com.github.aws404.polyguitest.grinder;

import com.github.aws404.polyguitest.ModInit;
import eu.pb4.polymer.item.ItemHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrinderBlockEntity extends LockableContainerBlockEntity {

    public static final int MAX_PROGRESS = 20;

    protected final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    public int progress = 0;

    public GrinderBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModInit.GRINDER_BLOCK_ENTITY, blockPos, blockState);
    }

    public boolean incrementProgress() {
        ItemStack input = this.getStack(0);
        if (!input.isEmpty()) {
            this.progress++;
            if (this.progress >= MAX_PROGRESS) {
                ItemStack outputStack = ItemStack.EMPTY;
                if (input.isOf(Items.GOLD_INGOT)) {
                    outputStack = new ItemStack(Items.GOLD_NUGGET, 9);
                }
                ItemStack currentOutput = this.getStack(1);
                if (currentOutput.isEmpty()) {
                    this.setStack(1, outputStack);
                } else if (ItemStack.canCombine(currentOutput, outputStack)) {
                    currentOutput.increment(outputStack.getCount());
                } else {
                    return false;
                }
                this.progress = 0;
                input.decrement(1);
            }

            return true;
        }

        return false;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, GrinderBlockEntity e) {
        if (world.getBlockState(blockPos.add(0, 1, 0)).isOf(ModInit.GRIND_WHEEL_BLOCK)) {
            e.incrementProgress();
        }
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.grinder");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }
}
