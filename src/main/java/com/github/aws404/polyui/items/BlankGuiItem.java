package com.github.aws404.polyui.items;

import com.github.aws404.polyui.PolyUIMod;
import eu.pb4.polymer.api.resourcepack.PolymerModelData;
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class BlankGuiItem extends AbstractGuiItem {

    private final PolymerModelData modelData;

    public BlankGuiItem(Settings settings) {
        super(settings);
        this.modelData = PolymerRPUtils.requestModel(Items.ITEM_FRAME, new Identifier(PolyUIMod.MODID, "gui/blank"));
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        return this.modelData.value();
    }
    
}
