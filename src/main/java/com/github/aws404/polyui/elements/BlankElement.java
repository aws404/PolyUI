package com.github.aws404.polyui.elements;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.util.PolyUiUtils;
import eu.pb4.sgui.api.SlotHolder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;

public class BlankElement implements GuiElementInterface {

    private static final ItemStack BLANK_GUI_ITEM = new ItemStack(PolyUIMod.BLANK_GUI_ITEM).setCustomName(LiteralText.EMPTY);

    @Override
    public ItemStack getItemStack() {
        return BLANK_GUI_ITEM.copy();
    }

    /**
     * @return a blank slot element
     */
    public static BlankElement blank() {
        return new BlankElement();
    }

    /**
     * Fill all the undefined slots of a holder with empty slots
     * @param holder the slot holder to fill
     */
    public static void fill(SlotHolder holder) {
        PolyUiUtils.fillEmptyWithElement(BlankElement::blank, holder);
    }
}
