package com.github.aws404.polyui.elements;

import com.github.aws404.polyui.items.ProgressGuiItem;
import com.github.aws404.polyui.registries.ProgressBar;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import eu.pb4.sgui.api.gui.GuiInterface;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class ProgressBarElement implements GuiElementInterface {

    public static final ProgressStateChangeCallback BLANK_STATE_CHANGE_CALLBACK = (newState, oldState) -> {};
    public static final ProgressStateLogic BLANK_PROGRESS_LOGIC = new ProgressStateLogic() {
        @Override
        public void tick(ProgressBarElement element) {
        }
    };

    protected final ClickCallback clickCallback;
    protected final ProgressStateChangeCallback progressStateChangeCallback;
    protected final ProgressStateLogic logic;
    protected final ProgressBar type;

    private int progress;

    public ProgressBarElement(ClickCallback clickCallback, ProgressStateChangeCallback progressStateChangeCallback, ProgressStateLogic logic, ProgressBar type, int startProgressState) {
        this.clickCallback = clickCallback;
        this.progressStateChangeCallback = progressStateChangeCallback;
        this.logic = logic;
        this.type = type;

        this.progress = startProgressState;
    }

    @Override
    public ItemStack getItemStackForDisplay(GuiInterface gui) {
        this.logic.tick(this);
        return GuiElementInterface.super.getItemStackForDisplay(gui);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack stack = ProgressGuiItem.getProgressStack(this.type);
        stack.setDamage(this.progress);
        return stack;
    }

    /**
     * @param progress A progress value between 0 (no progress bar) and 9 (full progress bar)
     */
    public void setProgress(int progress) {
        int newProgress = MathHelper.clamp(progress, 0, 9);
        if (newProgress != this.progress) {
            int oldProgress = this.progress;
            this.progress = newProgress;
            this.progressStateChangeCallback.progressStateChange(this, oldProgress);
        }
    }

    /**
     * Set the progress bar to the invalid (crossed out) state
     */
    public void setInvalid() {
        this.progress = 10;
    }

    /**
     * Get the current progress state of the element
     * The state will be an <code>int</code> from <code>0</code> (empty) to <code>9</code> (full) or <code>10</code> (invalid)
     * @return the current state
     */
    public int getProgress() {
        return this.progress;
    }

    @Override
    public ClickCallback getGuiCallback() {
        return this.clickCallback;
    }

    public interface ProgressStateChangeCallback {
        /**
         * The Progress State Change Callback is called prior to the bar changing state, so can be used to further manipulate this.
         * The state will be an <code>int</code> from <code>0</code> (empty) to <code>9</code> (full) or <code>10</code> (invalid)
         * @param element the ProgressArrowElement who's state has just changed
         * @param oldState the previous progress state
         */
        void progressStateChange(ProgressBarElement element, int oldState);
    }

    public static abstract class ProgressStateLogic {
        public abstract void tick(ProgressBarElement element);
    }

    public static class UniformStateLogic extends ProgressStateLogic {
        protected final int totalProgressTicks;

        protected int currentProgressTicks = 0;

        public UniformStateLogic(int totalProgressTicks) {
            this.totalProgressTicks = totalProgressTicks;
        }

        @Override
        public void tick(ProgressBarElement element) {
            if (this.totalProgressTicks != -1) {
                this.currentProgressTicks++;
                float pct = this.currentProgressTicks / (float) this.totalProgressTicks;
                if (pct <= 1) {
                    int newProgressState = (int) MathHelper.lerp(pct, 0, 9);

                    if (element.progress != newProgressState) {
                        element.setProgress(newProgressState);
                    }
                }
            }
        }
    }

    public static class SupplierStateLogic extends ProgressStateLogic {

        protected final Supplier<Float> stateSupplier;

        public SupplierStateLogic(Supplier<Float> stateSupplier) {
            this.stateSupplier = stateSupplier;
        }

        @Override
        public void tick(ProgressBarElement element) {
            element.setProgress((int) MathHelper.lerp(this.stateSupplier.get(), 0, 9));
        }
    }

    public static class RepeatingStateLogic extends ProgressStateLogic {
        protected final int totalProgressTicks;

        protected int currentProgressTicks = 0;

        public RepeatingStateLogic(int totalProgressTicks) {
            this.totalProgressTicks = totalProgressTicks;
        }

        @Override
        public void tick(ProgressBarElement element) {
            this.currentProgressTicks++;
            if (this.currentProgressTicks <= this.totalProgressTicks) {
                float pct = this.currentProgressTicks / (float) this.totalProgressTicks;
                int newProgressState = (int) MathHelper.lerp(pct, 0, 9);

                if (element.progress != newProgressState) {
                    element.setProgress(newProgressState);
                }
            } else {
                this.currentProgressTicks = 0;
            }
        }
    }
}
