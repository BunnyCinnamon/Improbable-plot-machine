package arekkuusu.implom.common.block;

import arekkuusu.implom.api.helper.WorldHelper;
import arekkuusu.implom.common.block.tile.TileBlastFurnacePipe;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;

public class BlockBlastFurnacePipe extends SixWayBlock {

    public BlockBlastFurnacePipe(Properties p_i48440_1_) {
        super(0.2F, p_i48440_1_.setAllowsSpawn(Blocks::neverAllowSpawn).setOpaque(Blocks::isntSolid).setSuffocates(Blocks::isntSolid).setBlocksVision(Blocks::isntSolid));
        this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.FALSE).with(EAST, Boolean.FALSE).with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(UP, Boolean.FALSE).with(DOWN, Boolean.FALSE));
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        WorldHelper.getTile(TileBlastFurnacePipe.class, world, pos).ifPresent(TileBlastFurnacePipe::setupConnections);
    }

    @Override
    public BlockState updatePostPlacement(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_1_.with(FACING_TO_PROPERTY_MAP.get(p_196271_2_), this.canConnectTo(p_196271_4_, p_196271_6_, p_196271_2_.getOpposite()));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.makeConnections(context.getWorld(), context.getPos());
    }

    public BlockState makeConnections(IBlockReader blockReader, BlockPos pos) {
        boolean down = canConnectTo(blockReader, pos, Direction.DOWN);
        boolean up = canConnectTo(blockReader, pos, Direction.UP);
        boolean north = canConnectTo(blockReader, pos, Direction.NORTH);
        boolean east = canConnectTo(blockReader, pos, Direction.EAST);
        boolean south = canConnectTo(blockReader, pos, Direction.SOUTH);
        boolean west = canConnectTo(blockReader, pos, Direction.WEST);
        return this.getDefaultState().with(DOWN, down).with(UP, up).with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west);
    }

    public boolean canConnectTo(IBlockReader blockReader, BlockPos pos, Direction neighbourDirection) {
        BlockPos neighbourPos = pos.offset(neighbourDirection);
        BlockState neighbourState = blockReader.getBlockState(neighbourPos);
        Block neighbourBlock = neighbourState.getBlock();

        boolean canConnect = false;
        if (neighbourBlock == ModBlocks.BLAST_FURNACE_PIPE.get()) {
            canConnect = true;
        } else if (neighbourBlock == ModBlocks.BLAST_FURNACE_PIPE_GAUGE.get()) {
            canConnect = neighbourState.get(HorizontalFaceBlock.FACE) != AttachFace.WALL ?
                    neighbourState.get(HorizontalFaceBlock.FACE) == AttachFace.CEILING ?
                            neighbourDirection == Direction.DOWN
                            : neighbourDirection == Direction.UP
                    : neighbourState.get(HorizontalFaceBlock.HORIZONTAL_FACING) == neighbourDirection.getOpposite();
        } else if (WorldHelper.getCapability(blockReader, neighbourPos, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, neighbourDirection.getOpposite()).isPresent()) {
            canConnect = true;
        }

        return canConnect;
    }

    @Override
    public boolean allowsMovement(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileBlastFurnacePipe();
    }
}
