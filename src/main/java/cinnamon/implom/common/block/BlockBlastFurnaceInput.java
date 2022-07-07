package cinnamon.implom.common.block;

import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.common.block.tile.TileBlastFurnaceInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class BlockBlastFurnaceInput extends HorizontalDirectionalBlock implements EntityBlock {

    protected BlockBlastFurnaceInput(Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.registerDefaultState(getStateDefinition().any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        WorldHelper.getTile(TileBlastFurnaceInput.class, level, pos).ifPresent(TileBlastFurnaceInput::setupConnections);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, arg.getHorizontalDirection());
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
        arg.add(HorizontalDirectionalBlock.FACING);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new TileBlastFurnaceInput(arg, arg2);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level arg, BlockState arg2, BlockEntityType<T> arg3) {
        return (BlockEntityTicker<T>) new TileBlastFurnaceInput.Ticking();
    }
}
