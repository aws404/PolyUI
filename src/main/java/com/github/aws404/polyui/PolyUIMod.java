package com.github.aws404.polyui;

import com.github.aws404.polyui.items.AbstractGuiItem;
import com.github.aws404.polyui.items.BlankGuiItem;
import com.github.aws404.polyui.items.ProgressGuiItem;
import com.github.aws404.polyui.items.SpriteGuiItem;
import com.github.aws404.polyui.items.registries.GuiSprites;
import com.github.aws404.polyui.items.registries.ProgressBars;
import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolyUIMod implements ModInitializer {
	public static final String MODID = "polyui";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final AbstractGuiItem PROGRESS_GUI_ITEM = Registry.register(Registry.ITEM, "polyui:progress", new ProgressGuiItem(new FabricItemSettings().maxDamage(10)));
	public static final AbstractGuiItem BLANK_GUI_ITEM = Registry.register(Registry.ITEM, "polyui:blank", new BlankGuiItem(new FabricItemSettings()));
	public static final AbstractGuiItem SPRITE_GUI_ITEM = Registry.register(Registry.ITEM, "polyui:sprite", new SpriteGuiItem(new FabricItemSettings()));

	@Override
	public void onInitialize() {
		ModMetadata thisData = FabricLoader.getInstance().getModContainer(MODID).orElseThrow(() -> new IllegalStateException("Could not find this mod? Please try again.")).getMetadata();
		ModMetadata polymerData = FabricLoader.getInstance().getModContainer("polymer").orElseThrow(() -> new IllegalStateException("Could not find Polymer! Make sure it is installed and try again.")).getMetadata();
		ModMetadata sguiData = FabricLoader.getInstance().getModContainer("sgui").orElseThrow(() -> new IllegalStateException("Could not find sgui! Make sure it is installed and try again.")).getMetadata();

		LOGGER.info("Starting {} ver. {}!", thisData.getName(), thisData.getVersion());
		LOGGER.info("- {} version: {}", polymerData.getName(), polymerData.getVersion());
		LOGGER.info("- {} version: {}", sguiData.getName(), sguiData.getVersion());

		ResourcePackUtils.addModAsAssetsSource(MODID);
	}
}
