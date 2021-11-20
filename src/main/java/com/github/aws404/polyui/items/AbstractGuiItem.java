package com.github.aws404.polyui.items;

import eu.pb4.polymer.item.VirtualItem;
import eu.pb4.polymer.resourcepack.CMDInfo;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

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
