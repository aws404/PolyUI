package com.github.aws404.polyguitest.grinder;

import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class GrindWheelBlock extends Block implements VirtualBlock {
    public GrindWheelBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.BLACKSTONE_WALL;
    }
}
