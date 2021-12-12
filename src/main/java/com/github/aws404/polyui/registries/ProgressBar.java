package com.github.aws404.polyui.registries;

import com.github.aws404.polyui.PolyUIMod;
import eu.pb4.polymer.api.resourcepack.PolymerModelData;
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public record ProgressBar(PolymerModelData modelData) {
    public static final ProgressBar ARROW = registerPolyUi("arrow");
    public static final ProgressBar FUEL = registerPolyUi("fuel");
    public static final ProgressBar LARGE_ARROW = registerPolyUi("large_arrow");

    /**
     * Register a new ProgressBar, the provided id also represents where the model
     * files for the progress bar should be located in the mod assets.
     * For example, the file for the id <code>foo:bar</code>, should be
     * located at <code>assets/foo/models/gui/progress/bar.json</code>
     * convention is that then the child model files will be in a directory of the
     * same path, i.e. <code>assets/foo/models/gui/progress/bar/..</code>
     * @param id the ProgressBar's id
     * @return the ProgressBar
     */
    public static ProgressBar register(String id) {
        Identifier barId = new Identifier(id);
        ProgressBar bar = new ProgressBar(PolymerRPUtils.requestModel(Items.WOODEN_SWORD, new Identifier(barId.getNamespace(), "gui/progress/" + barId.getPath())));
        return Registry.register(PolyUIMod.PROGRESS_BARS_REGISTRY, barId, bar);
    }

    private static ProgressBar registerPolyUi(String id) {
        return register(PolyUIMod.MODID + ":" + id);
    }

    public static PolymerModelData getModelData(Identifier id) {
        return PolyUIMod.PROGRESS_BARS_REGISTRY.get(id).modelData;
    }

    public static boolean containsModelData(Identifier id) {
        return PolyUIMod.PROGRESS_BARS_REGISTRY.containsId(id);
    }
}
