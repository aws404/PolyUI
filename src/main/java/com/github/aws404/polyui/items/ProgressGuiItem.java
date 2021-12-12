package com.github.aws404.polyui.items;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.registries.ProgressBar;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ProgressGuiItem extends AbstractGuiItem {

    public static final ProgressBar DEFAULT_PROGRESS_BAR = ProgressBar.ARROW;

    public ProgressGuiItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        ItemStack stack = super.getPolymerItemStack(itemStack, player);
        stack.addHideFlag(ItemStack.TooltipSection.UNBREAKABLE);
        stack.getOrCreateNbt().putBoolean("Unbreakable", true);
        return stack;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        Identifier type = new Identifier(itemStack.getOrCreateNbt().getString("Type"));
        return ProgressBar.getModelData(type).value();
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return Items.WOODEN_SWORD;
    }

    public static ItemStack getProgressStack(ProgressBar type) {
        ItemStack stack = new ItemStack(PolyUIMod.PROGRESS_GUI_ITEM);
        stack.getOrCreateNbt().putString("Type", PolyUIMod.PROGRESS_BARS_REGISTRY.getId(type).toString());
        return stack;
    }

}
