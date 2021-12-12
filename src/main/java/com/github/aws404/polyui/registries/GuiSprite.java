package com.github.aws404.polyui.registries;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.items.SpriteGuiItem;
import eu.pb4.polymer.api.resourcepack.PolymerModelData;
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public record GuiSprite(PolymerModelData data) {
    public static final GuiSprite BANNER = registerPolyUi("banner");
    public static final GuiSprite BLOCK = registerPolyUi("block");
    public static final GuiSprite BOOTS = registerPolyUi("boots");
    public static final GuiSprite BOTTLE = registerPolyUi("bottle");
    public static final GuiSprite CARPET = registerPolyUi("carpet");
    public static final GuiSprite CHESTPLATE = registerPolyUi("chestplate");
    public static final GuiSprite DYE = registerPolyUi("dye");
    public static final GuiSprite FUEL = registerPolyUi("fuel");
    public static final GuiSprite FULL = registerPolyUi("full");
    public static final GuiSprite HELMET = registerPolyUi("helmet");
    public static final GuiSprite HORSE_ARMOR = registerPolyUi("horse_armor");
    public static final GuiSprite INGOT = registerPolyUi("ingot");
    public static final GuiSprite LAPIS = registerPolyUi("lapis");
    public static final GuiSprite LEGGINGS = registerPolyUi("leggings");
    public static final GuiSprite PATTERN = registerPolyUi("pattern");
    public static final GuiSprite PLUS = registerPolyUi("plus");
    public static final GuiSprite POWDER = registerPolyUi("powder");
    public static final GuiSprite RECIPE_BOOK = registerPolyUi("recipe_book");
    public static final GuiSprite SADDLE = registerPolyUi("saddle");
    public static final GuiSprite SHIELD = registerPolyUi("shield");
    public static final GuiSprite TRASH = registerPolyUi("trash");

    public static final Identifier SPRITE_ITEM_SHEET = new Identifier(PolyUIMod.MODID, "sprite_item");

    public ItemStack getSpriteStack() {
        return SpriteGuiItem.getSpriteStack(this);
    }

    /**
     * Register a new GuiSprite, the provided id also represents where the model
     * files for the sprite should be located in the mod assets.
     * For example, the file for the id <code>foo:bar</code>, should be
     * located at <code>assets/foo/models/gui/sprites/bar.json</code>
     * @param id the GuiSprite's id
     * @return the GuiSprite
     */
    public static GuiSprite register(String id) {
        Identifier spriteId = new Identifier(id);
        GuiSprite sprite = new GuiSprite(PolymerRPUtils.requestModel(Items.ITEM_FRAME, new Identifier(spriteId.getNamespace(), "gui/sprites/" + spriteId.getPath())));
        return Registry.register(PolyUIMod.GUI_SPRITES_REGISTRY, spriteId, sprite);
    }

    private static GuiSprite registerPolyUi(String id) {
        return register(PolyUIMod.MODID + ":" + id);
    }

    public static PolymerModelData getModelData(Identifier id) {
        return PolyUIMod.GUI_SPRITES_REGISTRY.get(id).data;
    }

    public static PolymerModelData getModelData(GuiSprite sprite) {
        return sprite.data;
    }
}
