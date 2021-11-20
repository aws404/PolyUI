package com.github.aws404.polyui.items;

import com.github.aws404.polyui.PolyUIMod;
import eu.pb4.polymer.item.VirtualItem;
import eu.pb4.polymer.resourcepack.CMDInfo;
import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Int2ObjectBiMap;
import org.jetbrains.annotations.Nullable;

public class BlankGuiItem extends AbstractGuiItem {

    private final CMDInfo modelData;

    public BlankGuiItem(Settings settings) {
        super(settings);
        this.modelData = ResourcePackUtils.requestCustomModelData(Items.ITEM_FRAME, new Identifier(PolyUIMod.MODID, "gui/blank"));
    }

    @Override
    public int getCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.modelData.value();
    }
}
