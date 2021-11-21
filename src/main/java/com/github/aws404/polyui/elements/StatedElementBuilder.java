package com.github.aws404.polyui.elements;

import eu.pb4.sgui.api.elements.GuiElementBuilderInterface;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class StatedElementBuilder<T extends Enum<T>> implements GuiElementBuilderInterface<StatedElementBuilder<T>> {

    private final Map<T, GuiElementInterface> stateMap = new HashMap<>();
    private final Class<T> enumClass;
    private GuiElementInterface defaultValue;
    private Supplier<T> stateSupplier;
    @Nullable
    private GuiElementInterface.ClickCallback globalCallback = null;

    /**
     * Create a new stated element builder
     * @param enumClass the enum to base the states from
     */
    public StatedElementBuilder(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    /**
     * Add a state and element mapping
     * @param state the state to assign the element too
     * @param elementForState the element for the specified state
     * @return this builder
     */
    public StatedElementBuilder<T> addState(T state, GuiElementInterface elementForState) {
        stateMap.put(state, elementForState);
        return this;
    }

    /**
     * Add a state and element mapping
     * @param state the state to assign the element too
     * @param elementForState the element builder for the specified state
     * @return this builder
     */
    public StatedElementBuilder<T> addState(T state, GuiElementBuilderInterface<?> elementForState) {
        stateMap.put(state, elementForState.build());
        return this;
    }

    /**
     * Set the state supplier for the element
     * @param stateSupplier the supplier for the element
     * @return this builder
     */
    public StatedElementBuilder<T> setStateSupplier(Supplier<T> stateSupplier) {
        this.stateSupplier = stateSupplier;
        return this;
    }

    /**
     * Set the boolean state supplier for the element.
     * This method should only be used if the {@link Boolean} enum is used as the type.
     * @see StatedElementBuilder#forBoolean()
     * @throws ClassCastException if the builder is not of the {@link Boolean} enum
     * @param stateSupplier the boolean supplier for the element
     * @return this builder
     */
    public StatedElementBuilder<T> setBooleanStateSupplier(Supplier<java.lang.Boolean> stateSupplier) {
        this.stateSupplier = () -> (T) (stateSupplier.get() ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    /**
     * Set the default element, to be used if there is no mapping specified for the state
     * @param element the default element
     * @return this builder
     */
    public StatedElementBuilder<T> setDefaultElement(GuiElementInterface element) {
        this.defaultValue = element;
        return this;
    }

    /**
     * Set the default element, to be used if there is no mapping specified for the state
     * @param element the builder for the default element
     * @return this builder
     */
    public StatedElementBuilder<T> setDefaultElement(GuiElementBuilderInterface<?> element) {
        this.defaultValue = element.build();
        return this;
    }

    @Override
    public StatedElementBuilder<T> setCallback(GuiElementInterface.ClickCallback callback) {
        this.globalCallback = callback;
        return null;
    }

    /**
     * Builds the StatedElement
     * @throws IllegalArgumentException if the state supplier is not defined
     * @throws IllegalArgumentException if the builder is missing element mappings and no default element is supplied
     * @return the StatedElement
     */
    @Override
    public GuiElementInterface build() {
        if (this.stateSupplier == null) {
            throw new IllegalArgumentException("Cannot create a StatedElement with no state supplier!");
        }
        if (this.defaultValue == null) {
            T[] values = this.enumClass.getEnumConstants();
            for (T value : values) {
                if (!this.stateMap.containsKey(value)) {
                    throw new IllegalArgumentException("StatedElement does not have a default element and is missing for the state " + value + ", ether define one or a default");
                }
            }
        }
        return new StatedElement<>(this.stateSupplier, this.stateMap, this.globalCallback, this.defaultValue);
    }

    /**
     * Create a new boolean based StatedElementBuilder.
     * The states should be added according to the {@link Boolean} inner enum
     * @return a new builder
     */
    public static StatedElementBuilder<Boolean> forBoolean() {
        return new StatedElementBuilder<>(Boolean.class);
    }

    public enum Boolean {
        TRUE,
        FALSE
    }
}
