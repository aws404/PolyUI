package com.github.aws404.polyui.items;

import eu.pb4.polymer.api.item.PolymerItem;
import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGuiItem extends Item implements PolymerItem {

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
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.ITEM_FRAME;
    }
}
