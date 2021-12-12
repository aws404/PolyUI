package com.github.aws404.polyui.mixin;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.items.SpriteGuiItem;
import com.github.aws404.polyui.registries.GuiSprite;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow @Final public DefaultedList<Slot> slots;
    @Shadow public abstract void updateToClient();

    @Redirect(method = "sendContentUpdates", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getPacketStackAtSendContentUpdates(Slot slot) {
        if (!slot.hasStack() && slot.getBackgroundSprite() != null) {
            Pair<Identifier, Identifier> spriteData = slot.getBackgroundSprite();
            if (spriteData.getFirst() == GuiSprite.SPRITE_ITEM_SHEET) {
                return SpriteGuiItem.getSpriteStack(PolyUIMod.GUI_SPRITES_REGISTRY.get(spriteData.getSecond()));
            }
        }
        return slot.getStack();
    }

    @Redirect(method = "updateToClient", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getPacketStackAtUpdateToClient(Slot slot) {
        if (!slot.hasStack() && slot.getBackgroundSprite() != null) {
            Pair<Identifier, Identifier> spriteData = slot.getBackgroundSprite();
            if (spriteData.getFirst() == GuiSprite.SPRITE_ITEM_SHEET) {
                return SpriteGuiItem.getSpriteStack(PolyUIMod.GUI_SPRITES_REGISTRY.get(spriteData.getSecond()));
            }
        }
        return slot.getStack();
    }

    @Redirect(method = "getStacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getPacketStackAtGetStacks(Slot slot) {
        if (!slot.hasStack() && slot.getBackgroundSprite() != null) {
            Pair<Identifier, Identifier> spriteData = slot.getBackgroundSprite();
            if (spriteData.getFirst() == GuiSprite.SPRITE_ITEM_SHEET) {
                return SpriteGuiItem.getSpriteStack(PolyUIMod.GUI_SPRITES_REGISTRY.get(spriteData.getSecond()));
            }
        }
        return slot.getStack();
    }

    @Redirect(method = "syncState", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/slot/Slot;getStack()Lnet/minecraft/item/ItemStack;"))
    private ItemStack getPacketStackAtSyncState(Slot slot) {
        if (!slot.hasStack() && slot.getBackgroundSprite() != null) {
            Pair<Identifier, Identifier> spriteData = slot.getBackgroundSprite();
            if (spriteData.getFirst() == GuiSprite.SPRITE_ITEM_SHEET) {
                return SpriteGuiItem.getSpriteStack(PolyUIMod.GUI_SPRITES_REGISTRY.get(spriteData.getSecond()));
            }
        }
        return slot.getStack();
    }

    @Inject(method = "onSlotClick", at = @At("TAIL"))
    private void refreshClickedSlot(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (slotIndex >= 0 && slotIndex < this.slots.size()) {
            Slot slot = this.slots.get(slotIndex);
            if (!slot.hasStack() && slot.getBackgroundSprite() != null) {
                Pair<Identifier, Identifier> spriteData = slot.getBackgroundSprite();
                if (spriteData.getFirst() == GuiSprite.SPRITE_ITEM_SHEET) {
                    this.updateToClient();
                }
            }
        }
    }

}
