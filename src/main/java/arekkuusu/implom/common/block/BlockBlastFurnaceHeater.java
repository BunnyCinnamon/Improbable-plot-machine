package arekkuusu.implom.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class BlockBlastFurnaceHeater extends BlockBaseMultiBlock {

    protected BlockBlastFurnaceHeater(Properties builder) {
        super(builder);
    }

    @Override
    public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        return side == Direction.UP;
    }
}
