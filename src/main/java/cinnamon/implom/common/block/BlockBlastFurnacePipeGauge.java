package cinnamon.implom.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BlockBlastFurnacePipeGauge extends FaceAttachedHorizontalDirectionalBlock {

    protected static final VoxelShape AABB_CEILING_Z = Block.box(6.0D, 8.0D, 5.0D, 10.0D, 16.0D, 11.0D);
    protected static final VoxelShape AABB_CEILING_X = Block.box(5.0D, 8.0D, 6.0D, 11.0D, 16.0D, 10.0D);
    protected static final VoxelShape AABB_FLOOR_Z = Block.box(6.0D, 0.0D, 5.0D, 10.0D, 8.0D, 11.0D);
    protected static final VoxelShape AABB_FLOOR_X = Block.box(5.0D, 0.0D, 6.0D, 11.0D, 8.0D, 10.0D);
    protected static final VoxelShape AABB_NORTH = Block.box(6.0D, 5.0D, 8.0D, 10.0D, 11.0D, 16.0D);
    protected static final VoxelShape AABB_SOUTH = Block.box(6.0D, 5.0D, 0.0D, 10.0D, 11.0D, 8.0D);
    protected static final VoxelShape AABB_WEST = Block.box(8.0D, 5.0D, 6.0D, 16.0D, 11.0D, 10.0D);
    protected static final VoxelShape AABB_EAST = Block.box(0.0D, 5.0D, 6.0D, 8.0D, 11.0D, 10.0D);

    protected BlockBlastFurnacePipeGauge(BlockBehaviour.Properties builder) {
        super(builder);
        this.registerDefaultState(this.getStateDefinition().any().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(HORIZONTAL_FACING);
        switch (state.getValue(FACE)) {
            case FLOOR:
                if (direction.getAxis() == Direction.Axis.X) {
                    return AABB_FLOOR_X;
                }

                return AABB_FLOOR_Z;
            case WALL:
                switch (direction) {
                    case EAST:
                        return AABB_EAST;
                    case WEST:
                        return AABB_WEST;
                    case SOUTH:
                        return AABB_SOUTH;
                    case NORTH:
                }
                return AABB_NORTH;
            case CEILING:
            default:
                if (direction.getAxis() == Direction.Axis.X) {
                    return AABB_CEILING_X;
                } else {
                    return AABB_CEILING_Z;
                }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        Direction direction = state.getValue(HORIZONTAL_FACING).getOpposite();
        BlockPos blockpos = pos.relative(direction);
        BlockState blockState = levelReader.getBlockState(blockpos);
        return blockState.getBlock() == ModBlocks.BLAST_FURNACE_PIPE.get() || blockState.isFaceSturdy(levelReader, blockpos, direction.getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(HORIZONTAL_FACING, FACE);
    }
}
