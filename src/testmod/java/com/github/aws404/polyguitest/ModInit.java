package com.github.aws404.polyguitest;

import com.github.aws404.polyguitest.furnace.TriFurnaceBlock;
import com.github.aws404.polyguitest.furnace.TriFurnaceBlockEntity;
import com.github.aws404.polyguitest.grinder.GrindWheelBlock;
import com.github.aws404.polyguitest.grinder.GrinderBlock;
import com.github.aws404.polyguitest.grinder.GrinderBlockEntity;
import com.github.aws404.polyui.elements.BlankElement;
import com.github.aws404.polyui.elements.ProgressBarElement;
import com.github.aws404.polyui.elements.ProgressBarElementBuilder;
import com.github.aws404.polyui.elements.StatedElementBuilder;
import com.github.aws404.polyui.items.SpriteGuiItem;
import com.github.aws404.polyui.items.registries.GuiSprites;
import com.github.aws404.polyui.items.registries.ProgressBars;
import com.github.aws404.polyui.util.PolyUiUtils;
import com.github.aws404.polyui.util.SpriteSlot;
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import eu.pb4.polymer.item.VirtualBlockItem;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;

public class ModInit implements ModInitializer {

    public static Identifier GRINDER_PROGRESS_BAR = ProgressBars.register("grinder");

    public static Block TRI_FURNACE_BLOCK = Registry.register(Registry.BLOCK, "polyui_test:tri_furnace", new TriFurnaceBlock(FabricBlockSettings.of(Material.METAL)));
    public static BlockEntityType<TriFurnaceBlockEntity> TRI_FURNACE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "polyui_test:tri_furnace", FabricBlockEntityTypeBuilder.create(TriFurnaceBlockEntity::new, ModInit.TRI_FURNACE_BLOCK).build());

    public static Block GRIND_WHEEL_BLOCK = Registry.register(Registry.BLOCK, "polyui_test:grind_wheel", new GrindWheelBlock(FabricBlockSettings.of(Material.METAL)));
    public static Item GRIND_WHEEL_ITEM = Registry.register(Registry.ITEM, "polyui_test:grind_wheel", new VirtualBlockItem(GRIND_WHEEL_BLOCK, new FabricItemSettings().maxCount(1), Items.BLACKSTONE_WALL));

    public static Block GRINDER_BLOCK = Registry.register(Registry.BLOCK, "polyui_test:grinder", new GrinderBlock(FabricBlockSettings.of(Material.METAL)));
    public static BlockEntityType<GrinderBlockEntity> GRINDER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "polyui_test:grinder", FabricBlockEntityTypeBuilder.create(GrinderBlockEntity::new, ModInit.GRINDER_BLOCK).build());
    public static Item GRINDER_ITEM = Registry.register(Registry.ITEM, "polyui_test:grinder", new VirtualBlockItem(GRINDER_BLOCK, new FabricItemSettings(), Items.SMOKER));

    @Override
    public void onInitialize() {
        PolymerRPUtils.addAssetSource("polyui_test");

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("gui_test")
                    .executes(context -> {
                        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, context.getSource().getPlayer(), false);

                        Inventory inv = new SimpleInventory(3);
                        gui.setSlotRedirect(0, new SpriteSlot(inv, 0, GuiSprites.INGOT));
                        gui.setSlot(1, SpriteGuiItem.getSpriteStack(GuiSprites.PLUS));
                        gui.setSlotRedirect(2, new SpriteSlot(inv, 1, GuiSprites.POWDER));
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

                        gui.setSlotRedirect(9, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprites.HELMET, EquipmentSlot.HEAD));
                        gui.setSlotRedirect(10, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprites.CHESTPLATE, EquipmentSlot.CHEST));
                        gui.setSlotRedirect(11, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprites.LEGGINGS, EquipmentSlot.LEGS));
                        gui.setSlotRedirect(12, new ArmorSlot(context.getSource().getPlayer().getInventory(), GuiSprites.BOOTS, EquipmentSlot.FEET));
                        gui.setSlotRedirect(13, new SpriteSlot(context.getSource().getPlayer().getInventory(), PlayerInventory.OFF_HAND_SLOT, GuiSprites.SHIELD));

                        gui.setSlot(16, StatedElementBuilder.forBoolean()
                                .setBooleanStateSupplier(() -> gui.getPlayer().getAbilities().flying)
                                .addState(StatedElementBuilder.Boolean.TRUE, new GuiElementBuilder(Items.FEATHER).setName(new LiteralText("Flying!")))
                                .addState(StatedElementBuilder.Boolean.FALSE, new GuiElementBuilder(Items.BARRIER).setName(new LiteralText("Not Flying!")))
                        );

                        int slot = 27;
                        for (Identifier id : GuiSprites.getIds()) {
                            gui.setSlot(slot, SpriteGuiItem.getSpriteStack(id));
                            slot++;
                        }

                        for (Identifier id : ProgressBars.getIds()) {
                            gui.setSlot(slot, new ProgressBarElementBuilder().setProgressBarType(id).setProgressStateLogic(new ProgressBarElement.RepeatingStateLogic(80)));
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
                                         .addState(GameMode.SURVIVAL, GuiElementBuilder.from(SpriteGuiItem.getSpriteStack(GuiSprites.FULL))
                                                 .setCallback((index, type, action, gui1) -> gui1.getPlayer().currentScreenHandler.setCursorStack(ItemStack.EMPTY))
                                         )
                                         .addState(GameMode.CREATIVE, GuiElementBuilder.from(SpriteGuiItem.getSpriteStack(GuiSprites.TRASH))
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

        public ArmorSlot(Inventory inventory, Identifier sprite, EquipmentSlot slot) {
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
