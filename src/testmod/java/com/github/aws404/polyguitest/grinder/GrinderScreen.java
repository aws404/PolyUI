package com.github.aws404.polyguitest.grinder;

import com.github.aws404.polyui.elements.BlankElement;
import com.github.aws404.polyui.elements.ProgressBarElementBuilder;
import com.github.aws404.polyui.items.registries.GuiSprites;
import com.github.aws404.polyui.items.registries.ProgressBars;
import com.github.aws404.polyui.util.SpriteSlot;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

public class GrinderScreen extends SimpleGui {

    private final GrinderBlockEntity blockEntity;

    public GrinderScreen(ServerPlayerEntity player, GrinderBlockEntity blockEntity) {
        super(ScreenHandlerType.HOPPER, player, false);
        this.blockEntity = blockEntity;

        this.setTitle(blockEntity.getName());

        this.setSlotRedirect(0, new SpriteSlot(blockEntity, 0, GuiSprites.INGOT));

        this.setSlot(2, new ProgressBarElementBuilder()
                .setProgressType(ProgressBars.LARGE_ARROW)
                .setProgressSupplier(() -> blockEntity.progress / (float) GrinderBlockEntity.MAX_PROGRESS)
        );

        this.setSlotRedirect(4, new SpriteSlot(blockEntity, 1, GuiSprites.POWDER));

        BlankElement.fill(this);
    }
}
