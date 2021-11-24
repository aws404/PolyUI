package com.github.aws404.polyui.items.registries;

import com.github.aws404.polyui.PolyUIMod;
import eu.pb4.polymer.resourcepack.CMDInfo;
import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Int2ObjectBiMap;

import java.util.HashMap;
import java.util.Set;

public class ProgressBars {

    private static final HashMap<Identifier, Int2ObjectBiMap<CMDInfo>> MODEL_DATA = new HashMap<>();

    public static final Identifier ARROW = register("arrow");
    public static final Identifier FUEL = register("fuel");
    public static final Identifier LARGE_ARROW = register("large_arrow");

    public static Identifier register(String path) {
        Identifier id = new Identifier(PolyUIMod.MODID, "gui/progress/" + path);
        Int2ObjectBiMap<CMDInfo> data = Int2ObjectBiMap.create(11);
        for (int i = 0; i <= 10; i++) {
            data.add(ResourcePackUtils.requestCustomModelData(Items.ITEM_FRAME, new Identifier(PolyUIMod.MODID, "gui/progress/" + path + "/progress_" + i)));
        }

        MODEL_DATA.put(id, data);
        return id;
    }

    public static Int2ObjectBiMap<CMDInfo> getModelData(Identifier id) {
        return MODEL_DATA.get(id);
    }

    public static boolean containsModelData(Identifier id) {
        return MODEL_DATA.containsKey(id);
    }

    public static Set<Identifier> getIds() {
        return MODEL_DATA.keySet();
    }
}
