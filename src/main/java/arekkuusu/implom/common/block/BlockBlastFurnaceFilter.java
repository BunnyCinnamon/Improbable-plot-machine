package arekkuusu.implom.common.block;

import arekkuusu.implom.common.block.tile.TileMultiBlockImouto;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBlastFurnaceFilter extends DirectionalBlock {

    protected BlockBlastFurnaceFilter(Properties p_i48415_1_) {
        super(p_i48415_1_);
        setDefaultState(getDefaultState().with(DirectionalBlock.FACING, Direction.UP));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return getDefaultState().with(DirectionalBlock.FACING, p_196258_1_.getNearestLookingDirection());
    }

    @Override
    public void onBlockPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, @Nullable LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        BlockBaseMultiBlock.handlePlaceBlock(p_180633_1_, p_180633_2_);
    }

    @Override
    public void onReplaced(BlockState p_196243_1_, World p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
        BlockBaseMultiBlock.handleBreakBlock(p_196243_2_, p_196243_3_);
        super.onReplaced(p_196243_1_, p_196243_2_, p_196243_3_, p_196243_4_, p_196243_5_);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(DirectionalBlock.FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileMultiBlockImouto();
    }
}
