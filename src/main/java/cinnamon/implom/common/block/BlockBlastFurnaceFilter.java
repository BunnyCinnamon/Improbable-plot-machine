package cinnamon.implom.common.block;

import cinnamon.implom.common.block.tile.TileMultiBlockImouto;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class BlockBlastFurnaceFilter extends DirectionalBlock implements EntityBlock {

    protected BlockBlastFurnaceFilter(Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.registerDefaultState(getStateDefinition().any().setValue(DirectionalBlock.FACING, Direction.UP));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return defaultBlockState().setValue(DirectionalBlock.FACING, arg.getNearestLookingDirection());
    }

    @Override
    public void setPlacedBy(Level arg, BlockPos arg2, BlockState arg3, @org.jetbrains.annotations.Nullable LivingEntity arg4, ItemStack arg5) {
        BlockBaseMultiBlock.handlePlaceBlock(arg, arg2);
    }

    @Override
    public void onRemove(BlockState arg, Level arg2, BlockPos arg3, BlockState arg4, boolean bl) {
        BlockBaseMultiBlock.handleBreakBlock(arg2, arg3);
        super.onRemove(arg, arg2, arg3, arg4, bl);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(DirectionalBlock.FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new TileMultiBlockImouto(arg, arg2);
    }
}
