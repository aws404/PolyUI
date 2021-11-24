package com.github.aws404.polyui.items;

import com.github.aws404.polyui.PolyUIMod;
import eu.pb4.polymer.resourcepack.CMDInfo;
import eu.pb4.polymer.resourcepack.ResourcePackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class BlankGuiItem extends AbstractGuiItem {

    private final CMDInfo modelData;

    public BlankGuiItem(Settings settings) {
        super(settings);
        this.modelData = ResourcePackUtils.requestCustomModelData(Items.ITEM_FRAME, new Identifier(PolyUIMod.MODID, "gui/blank"));
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.modelData.value();
    }
    
}
