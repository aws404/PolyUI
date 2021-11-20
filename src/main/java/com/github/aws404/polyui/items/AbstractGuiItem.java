package com.github.aws404.polyui.items;

import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public abstract class AbstractGuiItem extends Item implements VirtualItem {

    public AbstractGuiItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return LiteralText.EMPTY;
    }

    @Override
    public Text getName() {
        return LiteralText.EMPTY;
    }

    @Override
    public Item getVirtualItem() {
        return Items.ITEM_FRAME;
    }
}
