package com.github.aws404.polyui.util;

import eu.pb4.sgui.api.SlotHolder;
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

    public static int getPlayerSlotFromGui(SlotHolder holder, int slot) {
        if (slot >= holder.getSize()) {
            int playerIndex;
            if (slot >= 32) {
                playerIndex = slot - 32;
            } else {
                playerIndex = slot + holder.getSize();
            }
            return playerIndex;
        }
        return -1;
    }
}
