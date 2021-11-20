package com.github.aws404.polyui.items.registries;

import com.github.aws404.polyui.PolyUIMod;
import eu.pb4.polymer.resourcepack.CMDInfo;
import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Set;

public class GuiSprites {

    private static final HashMap<Identifier, CMDInfo> MODEL_DATA = new HashMap<>();

    public static final Identifier BANNER = register("banner");
    public static final Identifier BOOTS = register("boots");
    public static final Identifier BOTTLE = register("bottle");
    public static final Identifier CARPET = register("carpet");
    public static final Identifier CHESTPLATE = register("chestplate");
    public static final Identifier DYE = register("dye");
    public static final Identifier FUEL = register("fuel");
    public static final Identifier FULL = register("full");
    public static final Identifier HELMET = register("helmet");
    public static final Identifier HORSE_ARMOR = register("horse_armor");
    public static final Identifier INGOT = register("ingot");
    public static final Identifier LAPIS = register("lapis");
    public static final Identifier LEGGINGS = register("leggings");
    public static final Identifier PATTERN = register("pattern");
    public static final Identifier PLUS = register("plus");
    public static final Identifier POWDER = register("powder");
    public static final Identifier SADDLE = register("saddle");
    public static final Identifier SHIELD = register("shield");
    public static final Identifier TRASH = register("trash");

    public static Identifier register(String path) {
        Identifier id = new Identifier(PolyUIMod.MODID, "gui/slot_backgrounds/" + path);
        MODEL_DATA.put(id, ResourcePackUtils.requestCustomModelData(Items.ITEM_FRAME, id));
        return id;
    }

    public static CMDInfo getModelData(Identifier id) {
        return MODEL_DATA.get(id);
    }

    public static boolean containsModelData(Identifier id) {
        return MODEL_DATA.containsKey(id);
    }

    public static Set<Identifier> getIds() {
        return MODEL_DATA.keySet();
    }
}
