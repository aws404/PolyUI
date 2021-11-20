package com.github.aws404.polyguitest.furnace;

import com.github.aws404.polyguitest.ModInit;
import eu.pb4.polymer.block.VirtualBlock;
import eu.pb4.sgui.virtual.inventory.VirtualScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TriFurnaceBlock extends BlockWithEntity implements VirtualBlock {
    public TriFurnaceBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModInit.TRI_FURNACE_BLOCK_ENTITY.instantiate(pos, state);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            TriFurnaceScreen screen = new TriFurnaceScreen((ServerPlayerEntity) player, (TriFurnaceBlockEntity) world.getBlockEntity(pos));
            screen.open();
            return ActionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModInit.TRI_FURNACE_BLOCK_ENTITY, TriFurnaceBlockEntity::tick);
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.FURNACE;
    }
}
