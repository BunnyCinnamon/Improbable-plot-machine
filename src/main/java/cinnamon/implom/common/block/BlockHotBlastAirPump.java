package cinnamon.implom.common.block;

import cinnamon.implom.common.block.tile.TileBlastFurnaceAirPump;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockHotBlastAirPump extends HorizontalDirectionalBlock implements EntityBlock {

    protected BlockHotBlastAirPump(BlockBehaviour.Properties builder) {
        super(builder);
        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return defaultBlockState().setValue(FACING, arg.getHorizontalDirection());
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
        arg.add(FACING);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new TileBlastFurnaceAirPump(arg, arg2);
    }
}
