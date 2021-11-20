package com.github.aws404.polyui.util;

import com.github.aws404.polyui.items.SpriteGuiItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class SpriteSlot extends Slot {

    private final Identifier sprite;

    public SpriteSlot(Inventory inventory, int index, Identifier sprite) {
        super(inventory, index, 0, 0);
        this.sprite = sprite;
    }

    @Override
    public Pair<Identifier, Identifier> getBackgroundSprite() {
        return Pair.of(SpriteGuiItem.SPRITE_ITEM_SHEET, sprite);
    }
}
