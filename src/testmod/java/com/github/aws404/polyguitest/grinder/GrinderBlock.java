package com.github.aws404.polyguitest.grinder;

import com.github.aws404.polyguitest.ModInit;
import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GrinderBlock extends BlockWithEntity implements VirtualBlock {
    public GrinderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient && world.getBlockEntity(pos) instanceof GrinderBlockEntity grinder) {
            if (hit.getSide() == Direction.UP) {
                if (grinder.incrementProgress()) {
                    return ActionResult.success(true);
                } else {
                    return ActionResult.FAIL;
                }
            } else {
                new GrinderScreen((ServerPlayerEntity) player, grinder).open();
                return ActionResult.success(true);
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModInit.GRINDER_BLOCK_ENTITY, GrinderBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModInit.GRINDER_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.SMOKER;
    }
}
