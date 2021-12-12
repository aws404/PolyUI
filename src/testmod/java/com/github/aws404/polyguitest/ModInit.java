package com.github.aws404.polyguitest;

import com.github.aws404.polyui.PolyUIMod;
import com.github.aws404.polyui.elements.BlankElement;
import com.github.aws404.polyui.elements.ProgressBarElement;
import com.github.aws404.polyui.elements.ProgressBarElementBuilder;
import com.github.aws404.polyui.elements.StatedElementBuilder;
import com.github.aws404.polyui.items.SpriteGuiItem;
import com.github.aws404.polyui.registries.GuiSprite;
import com.github.aws404.polyui.registries.ProgressBar;
import com.github.aws404.polyui.util.PolyUiUtils;
import com.github.aws404.polyui.util.SpriteSlot;
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.world.GameMode;

@SuppressWarnings("unused")
public class ModInit implements ModInitializer {

    public static ProgressBar GRINDER_PROGRESS_BAR = ProgressBar.register("polyui_test:grinder");
    public static GuiSprite EXCLAIM_GUI_SPRITE = GuiSprite.register("polyui_test:exclaim");

    @Override
    public void onInitialize() {
        PolymerRPUtils.addAssetSource("polyui_test");

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("gui_test")
                    .executes(context -> {
                        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, context.getSource().getPlayer(), false);

                        Inventory inv = new SimpleInventory(3);
                        gui.setSlotRedirect(0, new SpriteSlot(inv, 0, GuiSprite.INGOT));
                        gui.setSlot(1, SpriteGuiItem.getSpriteStack(GuiSprite.PLUS));
                        gui.setSlotRedirect(2, new SpriteSlot(inv, 1, GuiSprite.POWDER));
                        gui.setSlot(3, BlankElement.blank());
                        gui.setSlot(4, new ProgressBarElementBuilder()
                                .setProgressStateChangeCallback((element, oldState) -> {
                                    if (element.getProgress() >= 9) {
                                        inv.setStack(2, inv.getStack(0).copy());
                                        element.setInvalid();
                                    }
                                })
                                .setProgressTotalTicks(20 * 10)
                        );
                        gui.setSlot(5, BlankElement.blank());
                        gui.setSlotRedirect(6, new Slot(inv, 2, 0, 0));

                        gui.setSlotRedirect(9, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprite.HELMET, EquipmentSlot.HEAD));
                        gui.setSlotRedirect(10, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprite.CHESTPLATE, EquipmentSlot.CHEST));
                        gui.setSlotRedirect(11, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprite.LEGGINGS, EquipmentSlot.LEGS));
                        gui.setSlotRedirect(12, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprite.BOOTS, EquipmentSlot.FEET));
                        gui.setSlotRedirect(13, new SpriteSlot(context.getSource().getPlayer().getInventory(), PlayerInventory.OFF_HAND_SLOT, GuiSprite.SHIELD));

                        gui.setSlot(16, StatedElementBuilder.forBoolean()
                                .setBooleanStateSupplier(() -> gui.getPlayer().getAbilities().flying)
                                .addState(StatedElementBuilder.Boolean.TRUE, new GuiElementBuilder(Items.FEATHER).setName(new LiteralText("Flying!")))
                                .addState(StatedElementBuilder.Boolean.FALSE, new GuiElementBuilder(Items.BARRIER).setName(new LiteralText("Not Flying!")))
                        );

                        int slot = 18;
                        for (GuiSprite sprite : PolyUIMod.GUI_SPRITES_REGISTRY) {
                            gui.setSlot(slot, sprite.getSpriteStack());
                            slot++;
                        }

                        slot++;

                        for (ProgressBar progressBar : PolyUIMod.PROGRESS_BARS_REGISTRY) {
                            gui.setSlot(slot, new ProgressBarElementBuilder()
                                    .setProgressBarType(progressBar)
                                    .setProgressStateLogic(new ProgressBarElement.RepeatingStateLogic(80)));
                            slot++;
                        }

                        gui.open();
                        return 1;
                    })
            );

            dispatcher.register(CommandManager.literal("trash")
                    .executes(context -> {
                         new SimpleGui(ScreenHandlerType.HOPPER, context.getSource().getPlayer(), false) {
                             {
                                 this.setTitle(new LiteralText("              Trash Can"));
                                 this.setSlot(2, new StatedElementBuilder<>(GameMode.class)
                                         .setStateSupplier(player.interactionManager::getGameMode)
                                         .setDefaultElement(new GuiElementBuilder(Items.BARRIER, 1)
                                                 .setName((MutableText) LiteralText.EMPTY)
                                         )
                                         .addState(GameMode.SURVIVAL, GuiElementBuilder.from(SpriteGuiItem.getSpriteStack(GuiSprite.FULL))
                                                 .setCallback((index, type, action, gui1) -> gui1.getPlayer().currentScreenHandler.setCursorStack(ItemStack.EMPTY))
                                         )
                                         .addState(GameMode.CREATIVE, GuiElementBuilder.from(SpriteGuiItem.getSpriteStack(GuiSprite.TRASH))
                                                 .setCallback((index, type, action, gui1) -> gui1.getPlayer().currentScreenHandler.setCursorStack(ItemStack.EMPTY))
                                         )
                                 );
                                BlankElement.fill(this);
                                this.open();
                            }

                             @Override
                             public boolean onAnyClick(int index, ClickType type, SlotActionType action) {
                                 if (index > this.getSize() && type.shift) {
                                     int playerIndex = PolyUiUtils.getPlayerSlotFromGui(this, index);
                                     this.player.getInventory().setStack(playerIndex, ItemStack.EMPTY);
                                     return false;
                                 }
                                 return super.onAnyClick(index, type, action);
                             }
                         };

                        return 1;
                    })
            );
        });
    }

    private static class ArmorSlot extends SpriteSlot {

        public final EquipmentSlot slot;

        public ArmorSlot(Inventory inventory, GuiSprite sprite, EquipmentSlot slot) {
            super(inventory, 36 + slot.getEntitySlotId(), sprite);
            this.slot = slot;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return slot == MobEntity.getPreferredEquipmentSlot(stack);
        }

        public boolean canTakeItems(PlayerEntity playerEntity) {
            ItemStack itemStack = this.getStack();
            return (itemStack.isEmpty() || playerEntity.isCreative() || !EnchantmentHelper.hasBindingCurse(itemStack)) && super.canTakeItems(playerEntity);
        }

        public int getMaxItemCount() {
            return 1;
        }

    }
}
