package com.github.aws404.polyui.util;

import com.github.aws404.polyui.elements.BlankElement;
import eu.pb4.sgui.api.SlotHolder;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.elements.GuiElementInterface;

import java.util.function.Supplier;

public class PolyUiUtils {

    public static void fillEmptyWithElement(GuiElementBuilderInterface<?> builder, SlotHolder holder) {
        fillEmptyWithElement(builder::build, holder);
    }

    public static void fillEmptyWithElement(Supplier<GuiElementInterface> elementSupplier, SlotHolder holder) {
        for (int i = 0; i < holder.getSize(); i++) {
            if (holder.getSlot(i) == null && holder.getSlotRedirect(i) == null) {
                holder.setSlot(i, elementSupplier.get());
            }
        }
    }
}
