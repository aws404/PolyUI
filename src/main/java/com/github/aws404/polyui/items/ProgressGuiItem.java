package com.github.aws404.polyui.items;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.items.registries.ProgressBars;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ProgressGuiItem extends AbstractGuiItem {

    private final Identifier defaultBar;

    public ProgressGuiItem(Settings settings) {
        super(settings);
        this.defaultBar = ProgressBars.ARROW;
    }

    @Override
    public int getPolymerCustomModelData(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        Identifier type = new Identifier(itemStack.getOrCreateNbt().getString("Type"));
        if (!ProgressBars.containsModelData(type)) {
            PolyUIMod.LOGGER.error("Tried to use an un-registered progress bar '{}', reverting to default", type.toString());
            itemStack.getOrCreateNbt().putString("Type", this.defaultBar.toString());
            type = this.defaultBar;
        }

        return ProgressBars.getModelData(type).get(itemStack.getDamage()).value();
    }

    public static ItemStack getProgressStack(Identifier type) {
        ItemStack stack = new ItemStack(PolyUIMod.PROGRESS_GUI_ITEM);
        stack.getOrCreateNbt().putString("Type", type.toString());
        return stack;
    }

}
