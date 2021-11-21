package com.github.aws404.polyui.elements;

import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.GuiInterface;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class StatedElement<T extends Enum<T>> implements GuiElementInterface {

    private final Supplier<T> stateSupplier;
    private final Map<T, GuiElementInterface> stateMap;
    @Nullable
    private final ClickCallback callback;
    private final GuiElementInterface defaultValue;

    public StatedElement(Supplier<T> stateSupplier, Map<T, GuiElementInterface> stateMap, @Nullable ClickCallback callback, GuiElementInterface defaultValue) {
        this.stateSupplier = stateSupplier;
        this.stateMap = stateMap;
        this.callback = callback;
        this.defaultValue = defaultValue;
    }

    @Override
    public ClickCallback getGuiCallback() {
        return this.callback != null ? this.callback : this.stateMap.getOrDefault(this.stateSupplier.get(), this.defaultValue).getGuiCallback();
    }

    @Override
    public ItemStack getItemStackForDisplay(GuiInterface gui) {
        return this.stateMap.getOrDefault(this.stateSupplier.get(), this.defaultValue).getItemStackForDisplay(gui);
    }

    @Override
    public ItemStack getItemStack() {
        return this.stateMap.getOrDefault(this.stateSupplier.get(), this.defaultValue).getItemStack();
    }
}
