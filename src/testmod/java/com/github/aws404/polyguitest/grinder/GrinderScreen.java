package com.github.aws404.polyguitest.grinder;

import com.github.aws404.polyguitest.ModInit;
import com.github.aws404.polyui.elements.BlankElement;
import com.github.aws404.polyui.elements.ProgressBarElement;
import com.github.aws404.polyui.elements.ProgressBarElementBuilder;
import com.github.aws404.polyui.elements.StatedElementBuilder;
import com.github.aws404.polyui.items.registries.GuiSprites;
import com.github.aws404.polyui.items.registries.ProgressBars;
import com.github.aws404.polyui.util.SpriteSlot;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class GrinderScreen extends SimpleGui {

    private final GrinderBlockEntity blockEntity;

    public GrinderScreen(ServerPlayerEntity player, GrinderBlockEntity blockEntity) {
        super(ScreenHandlerType.HOPPER, player, false);
        this.blockEntity = blockEntity;

        this.setTitle(blockEntity.getName());

        this.setSlotRedirect(0, new SpriteSlot(blockEntity, 0, GuiSprites.INGOT));

        this.setSlot(2, new StatedElementBuilder<>(GrinderBlockEntity.GrindingType.class)
                .setStateSupplier(() -> this.blockEntity.grindingType)
                .addState(GrinderBlockEntity.GrindingType.AUTOMATIC, new ProgressBarElementBuilder()
                        .setProgressBarType(ModInit.GRINDER_PROGRESS_BAR)
                        .setProgressStateLogic(new GrinderProgressStateLogic())
                )
                .addState(GrinderBlockEntity.GrindingType.MANUAL, new ProgressBarElementBuilder()
                        .setProgressBarType(ProgressBars.LARGE_ARROW)
                        .setProgressStateLogic(new GrinderProgressStateLogic())
                )
        );

        this.setSlotRedirect(4, new SpriteSlot(blockEntity, 1, GuiSprites.POWDER) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        BlankElement.fill(this);
    }

    private class GrinderProgressStateLogic extends ProgressBarElement.ProgressStateLogic {

        @Override
        public void tick(ProgressBarElement element) {
            int progress = GrinderScreen.this.blockEntity.progress;
            if (progress < 0) {
                element.setInvalid();
            } else {
                element.setProgress((int) MathHelper.lerp((progress / (float) GrinderBlockEntity.MAX_PROGRESS), 0, 9));
            }
        }
    }
}
