package arekkuusu.implom.common.block;

import arekkuusu.implom.api.helper.WorldHelper;
import arekkuusu.implom.common.block.tile.TileBlastFurnaceAirVent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHotBlastAirVent extends BlockBaseMultiBlock {

    protected BlockHotBlastAirVent(Properties builder) {
        super(builder);
        setDefaultState(getDefaultState().with(BlockBaseMultiBlock.ACTIVE, false));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote && pos.offset(Direction.UP).equals(fromPos)) {
            WorldHelper.getTile(TileBlastFurnaceAirVent.class, worldIn, pos).ifPresent(t -> {
                boolean isPowered = worldIn.isBlockPowered(pos);
                if (isPowered) {
                    if (!t.isStructureActive) {
                        t.okaeriOniichan();
                    } else {
                        t.invalidateStructure();
                    }
                }
            });
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return side == Direction.UP;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockBaseMultiBlock.ACTIVE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileBlastFurnaceAirVent();
    }
}
