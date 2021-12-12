package com.github.aws404.polyui.elements;

import com.github.aws404.polyui.items.ProgressGuiItem;
import com.github.aws404.polyui.registries.ProgressBar;
import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.elements.GuiElementInterface;

import java.util.function.Supplier;

public class ProgressBarElementBuilder implements GuiElementBuilderInterface<ProgressBarElementBuilder> {

    private GuiElementInterface.ClickCallback clickCallback = GuiElementInterface.EMPTY_CALLBACK;
    private ProgressBarElement.ProgressStateChangeCallback completeCallback = ProgressBarElement.BLANK_STATE_CHANGE_CALLBACK;
    private ProgressBarElement.ProgressStateLogic stateLogic = ProgressBarElement.BLANK_PROGRESS_LOGIC;
    private int startProgress = 0;
    private ProgressBar type = ProgressGuiItem.DEFAULT_PROGRESS_BAR;

    /**
     * Set the progress logic of the bar to increase uniformly, according to the supplied time
     * @param progressTicks the time in ticks for the bar to fill
     * @return this element builder
     */
    public ProgressBarElementBuilder setProgressTotalTicks(int progressTicks) {
        this.stateLogic = new ProgressBarElement.UniformStateLogic(progressTicks);
        return this;
    }

    /**
     * Set the progress logic of the bar to obtain the percent from the supplier
     * @param pctSupplier a <code>Supplier</code> which should return a value from <code>0.0</code> (empty) to <code>1.0</code> (full)
     * @return this element builder
     */
    public ProgressBarElementBuilder setProgressSupplier(Supplier<Float> pctSupplier) {
        this.stateLogic = new ProgressBarElement.SupplierStateLogic(pctSupplier);
        return this;
    }

    /**
     * Define custom progress logic for the bar
     * @param stateLogic the custom state logic
     * @return this element builder
     */
    public ProgressBarElementBuilder setProgressStateLogic(ProgressBarElement.ProgressStateLogic stateLogic) {
        this.stateLogic = stateLogic;
        return this;
    }

    /**
     * Set a callback to be ran each time the progress bar changes progress state.
     * The callback contains the old progress state, an <code>int</code> from <code>0</code> (empty) to <code>9</code> (full) or <code>10</code> (invalid)
     * @see ProgressBarElement.ProgressStateChangeCallback#progressStateChange(ProgressBarElement, int)
     * @param progressStateChangeCallback the callback to be used
     * @return this element builder
     */
    public ProgressBarElementBuilder setProgressStateChangeCallback(ProgressBarElement.ProgressStateChangeCallback progressStateChangeCallback) {
        this.completeCallback = progressStateChangeCallback;
        return this;
    }

    /**
     * Set the type of progress bar to use. This must be a registered progress bar type so the textures can all be loaded correctly.
     * @see ProgressBar#register(String) for more regarding registering progress bar types
     * @param type the registered progress bar <code>Identifier</code>
     * @return this element builder
     */
    public ProgressBarElementBuilder setProgressBarType(ProgressBar type) {
        this.type = type;
        return this;
    }

    /**
     * Set the starting progress state of the bar.
     * This should be a valid progress state, ie. <code>0</code> (empty) to <code>9</code> (full) or <code>10</code> (invalid)
     * @param startProgress the progress to start the bar at
     * @return this element builder
     */
    public ProgressBarElementBuilder setStartProgressState(int startProgress) {
        this.startProgress = startProgress;
        return this;
    }

    @Override
    public ProgressBarElementBuilder setCallback(GuiElementInterface.ClickCallback callback) {
        this.clickCallback = callback;
        return this;
    }

    @Override
    public GuiElementInterface build() {
        return new ProgressBarElement(this.clickCallback, this.completeCallback, this.stateLogic, this.type, this.startProgress);
    }
}
