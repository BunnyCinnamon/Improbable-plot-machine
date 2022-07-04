package cinnamon.implom.common.block;

import cinnamon.implom.api.helper.WorldHelper;
import cinnamon.implom.common.block.tile.TileBlastFurnacePipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class BlockBlastFurnacePipe extends PipeBlock implements EntityBlock {

    public BlockBlastFurnacePipe(BlockBehaviour.Properties p_i48440_1_) {
        super(0.2F, p_i48440_1_.randomTicks().noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, Boolean.FALSE).setValue(EAST, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE));
    }

    @Override
    public void neighborChanged(BlockState arg, Level arg2, BlockPos arg3, Block arg4, BlockPos arg5, boolean bl) {
        if (!arg2.isClientSide()) {
            WorldHelper.getTile(TileBlastFurnacePipe.class, arg2, arg3).ifPresent(TileBlastFurnacePipe::setupConnections);
        }
    }

    @Override
    public BlockState updateShape(BlockState arg, Direction arg2, BlockState arg3, LevelAccessor arg4, BlockPos arg5, BlockPos arg6) {
        boolean bl = this.canConnectTo(arg4, arg5, arg2);
        return arg.setValue(PROPERTY_BY_DIRECTION.get(arg2), bl);
    }

    public boolean canConnectTo(LevelAccessor level, BlockPos pos, Direction neighbourDirection) {
        BlockPos neighbourPos = pos.relative(neighbourDirection);
        BlockState neighbourState = level.getBlockState(neighbourPos);
        Block neighbourBlock = neighbourState.getBlock();

        boolean canConnect = false;
        if (neighbourBlock == ModBlocks.BLAST_FURNACE_PIPE.get()) {
            canConnect = true;
        } else if (neighbourBlock == ModBlocks.BLAST_FURNACE_PIPE_GAUGE.get()) {
            canConnect = neighbourState.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) != AttachFace.WALL ?
                    neighbourState.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) == AttachFace.CEILING ?
                            neighbourDirection == Direction.DOWN
                            : neighbourDirection == Direction.UP
                    : neighbourState.getValue(HorizontalDirectionalBlock.FACING) == neighbourDirection;
        } else if (WorldHelper.getCapability(level, neighbourPos, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite()).isPresent()) {
            canConnect = true;
        }

        return canConnect;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new TileBlastFurnacePipe(arg, arg2);
    }
}
