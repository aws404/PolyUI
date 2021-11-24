package com.github.aws404.polyui.items;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.items.registries.GuiSprites;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SpriteGuiItem extends AbstractGuiItem {

    public static final Identifier SPRITE_ITEM_SHEET = new Identifier(PolyUIMod.MODID, "sprite_item");

    private final Identifier defaultSprite;

    public SpriteGuiItem(Settings settings) {
        super(settings);
        this.defaultSprite = GuiSprites.INGOT;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        Identifier sprite = new Identifier(itemStack.getOrCreateNbt().getString("Sprite"));

        if (!GuiSprites.containsModelData(sprite)) {
            PolyUIMod.LOGGER.error("Tried to use an un-registered gui sprite '{}', reverting to default", sprite.toString());
            itemStack.getOrCreateNbt().putString("Type", this.defaultSprite.toString());
            sprite = this.defaultSprite;
        }

        return GuiSprites.getModelData(sprite).value();
    }

    public static ItemStack getSpriteStack(Identifier id) {
        ItemStack stack = new ItemStack(PolyUIMod.SPRITE_GUI_ITEM);
        stack.getOrCreateNbt().putString("Sprite", id.toString());
        return stack;
    }

}
