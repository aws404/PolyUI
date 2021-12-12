package com.github.aws404.polyui.items;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.registries.GuiSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SpriteGuiItem extends AbstractGuiItem {

    public static final GuiSprite DEFAULT_SPRITE = GuiSprite.BLOCK;

    public SpriteGuiItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        Identifier sprite = new Identifier(itemStack.getOrCreateNbt().getString("Sprite"));
        return GuiSprite.getModelData(sprite).value();
    }

    public static ItemStack getSpriteStack(GuiSprite id) {
        ItemStack stack = new ItemStack(PolyUIMod.SPRITE_GUI_ITEM);
        stack.getOrCreateNbt().putString("Sprite", PolyUIMod.GUI_SPRITES_REGISTRY.getId(id).toString());
        return stack;
    }

}
