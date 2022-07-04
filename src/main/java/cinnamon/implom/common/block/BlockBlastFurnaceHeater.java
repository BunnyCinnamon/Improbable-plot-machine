package cinnamon.implom.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class BlockBlastFurnaceHeater extends BlockBaseMultiBlock {

    protected BlockBlastFurnaceHeater(Properties builder) {
        super(builder);
    }

    @Override
    public boolean isFireSource(BlockState state, LevelReader level, BlockPos pos, Direction direction) {
        return direction == Direction.UP;
    }
}
