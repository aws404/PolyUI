package com.github.aws404.polyui;

import com.github.aws404.polyui.items.AbstractGuiItem;
import com.github.aws404.polyui.items.BlankGuiItem;
import com.github.aws404.polyui.items.ProgressGuiItem;
import com.github.aws404.polyui.items.SpriteGuiItem;
import com.github.aws404.polyui.registries.GuiSprite;
import com.github.aws404.polyui.registries.ProgressBar;
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PolyUIMod implements DedicatedServerModInitializer {
	public static final String MODID = "polyui";

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final Registry<GuiSprite> GUI_SPRITES_REGISTRY = FabricRegistryBuilder.createDefaulted(GuiSprite.class, new Identifier(MODID, "gui_sprites"), new Identifier(PolyUIMod.MODID, "block")).buildAndRegister();
	public static final Registry<ProgressBar> PROGRESS_BARS_REGISTRY = FabricRegistryBuilder.createDefaulted(ProgressBar.class, new Identifier(MODID, "progress_bars"), new Identifier(PolyUIMod.MODID, "arrow")).buildAndRegister();

	public static final AbstractGuiItem PROGRESS_GUI_ITEM = Registry.register(Registry.ITEM, "polyui:progress", new ProgressGuiItem(new FabricItemSettings().maxDamage(10)));
	public static final AbstractGuiItem BLANK_GUI_ITEM = Registry.register(Registry.ITEM, "polyui:blank", new BlankGuiItem(new FabricItemSettings()));
	public static final AbstractGuiItem SPRITE_GUI_ITEM = Registry.register(Registry.ITEM, "polyui:sprite", new SpriteGuiItem(new FabricItemSettings()));

	@Override
	public void onInitializeServer() {
		ModMetadata thisData = FabricLoader.getInstance().getModContainer(MODID).orElseThrow(() -> new IllegalStateException("Could not find this mod? Please try again.")).getMetadata();
		LOGGER.info("Starting {} ver. {}!", thisData.getName(), thisData.getVersion());

		PolymerRPUtils.addAssetSource(MODID);
	}
}
