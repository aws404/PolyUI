package com.github.aws404.polyui.util;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.registries.GuiSprite;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class SpriteSlot extends Slot {

    private final GuiSprite sprite;

    public SpriteSlot(Inventory inventory, int index, GuiSprite sprite) {
        super(inventory, index, 0, 0);
        this.sprite = sprite;
    }

    @Override
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        return Pair.of(GuiSprite.SPRITE_ITEM_SHEET, PolyUIMod.GUI_SPRITES_REGISTRY.getId(this.sprite));
    }
}
