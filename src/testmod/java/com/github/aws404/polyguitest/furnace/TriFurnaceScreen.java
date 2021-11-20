package com.github.aws404.polyguitest.furnace;

import com.github.aws404.polyui.elements.BlankElement;
import com.github.aws404.polyui.elements.ProgressBarElementBuilder;
import com.github.aws404.polyui.items.registries.GuiSprites;
import com.github.aws404.polyui.items.registries.ProgressBars;
import com.github.aws404.polyui.util.SpriteSlot;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class TriFurnaceScreen extends SimpleGui {

    private final TriFurnaceBlockEntity blockEntity;

    public TriFurnaceScreen(ServerPlayerEntity player, TriFurnaceBlockEntity blockEntity) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.blockEntity = blockEntity;

        this.setLockPlayerInventory(false);
        this.setTitle(new LiteralText("Tri-Furnace"));

        // Fuel Slot
        this.setSlotRedirect(19, new FurnaceFuelSlot(this.blockEntity, 0));
        this.setSlot(10, new ProgressBarElementBuilder().setProgressType(ProgressBars.FUEL).setProgressSupplier(this::getFuelPercent));

        // Furnace 1
        this.setSlotRedirect(3, new Slot(this.blockEntity, 1, 0, 0));
        this.setSlot(5, new ProgressBarElementBuilder().setProgressSupplier(() -> this.getProgressPercent(0)));
        this.setSlotRedirect(7, new FurnaceOutputSlot(this.blockEntity, 2, player, 0));

        // Furnace 2
        this.setSlotRedirect(12, new Slot(this.blockEntity, 3, 0, 0));
        this.setSlot(14, new ProgressBarElementBuilder().setProgressSupplier(() ->this.getProgressPercent(1)));
        this.setSlotRedirect(16, new FurnaceOutputSlot(this.blockEntity, 4, player, 1));

        // Furnace 3
        this.setSlotRedirect(21, new Slot(this.blockEntity, 5, 0, 0));
        this.setSlot(23, new ProgressBarElementBuilder().setProgressSupplier(() -> this.getProgressPercent(2)));
        this.setSlotRedirect(25, new FurnaceOutputSlot(this.blockEntity, 6, player, 2));

        BlankElement.fill(this);
    }

    public float getProgressPercent(int index) {
        return this.blockEntity.cookTimes[index] / (float) this.blockEntity.cookTimeTotals[index];
    }

    public float getFuelPercent() {
        float i = this.blockEntity.fuelTime;
        if (i == 0) {
            i = 200;
        }

        return this.blockEntity.burnTime / i;
    }

    public static class FurnaceFuelSlot extends SpriteSlot {

        public FurnaceFuelSlot(Inventory inventory, int index) {
            super(inventory, index, GuiSprites.FUEL);
        }

        public boolean canInsert(ItemStack stack) {
            return AbstractFurnaceBlockEntity.canUseAsFuel(stack) || isBucket(stack);
        }

        public int getMaxItemCount(ItemStack stack) {
            return isBucket(stack) ? 1 : super.getMaxItemCount(stack);
        }

        public static boolean isBucket(ItemStack stack) {
            return stack.isOf(Items.BUCKET);
        }
    }

    private static class FurnaceOutputSlot extends Slot {

        private final ServerPlayerEntity player;
        private final int furnaceIndex;
        private int amount;

        public FurnaceOutputSlot(Inventory inventory, int index, ServerPlayerEntity player, int furnaceIndex) {
            super(inventory, index, 0, 0);
            this.player = player;
            this.furnaceIndex = furnaceIndex;
        }

        public boolean canInsert(ItemStack stack) {
            return false;
        }

        public ItemStack takeStack(int amount) {
            if (this.hasStack()) {
                this.amount += Math.min(amount, this.getStack().getCount());
            }

            return super.takeStack(amount);
        }

        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.onCrafted(stack);
            super.onTakeItem(player, stack);
        }

        protected void onCrafted(ItemStack stack, int amount) {
            this.amount += amount;
            this.onCrafted(stack);
        }

        protected void onCrafted(ItemStack stack) {
            stack.onCraft(this.player.world, this.player, this.amount);
            if (this.inventory instanceof TriFurnaceBlockEntity furnace) {
                furnace.dropExperienceForRecipesUsed(this.player, this.furnaceIndex);
            }

            this.amount = 0;
        }
    }
}
