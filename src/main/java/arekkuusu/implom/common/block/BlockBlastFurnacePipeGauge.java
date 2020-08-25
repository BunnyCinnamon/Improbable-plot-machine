package arekkuusu.implom.common.block;

import arekkuusu.implom.IPM;
import arekkuusu.implom.api.helper.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.Objects;

public class BlockBlastFurnacePipeGauge extends HorizontalFaceBlock {

    protected static final VoxelShape AABB_CEILING_Z = Block.makeCuboidShape(6.0D, 8.0D, 5.0D, 10.0D, 16.0D, 11.0D);
    protected static final VoxelShape AABB_CEILING_X = Block.makeCuboidShape(5.0D, 8.0D, 6.0D, 11.0D, 16.0D, 10.0D);
    protected static final VoxelShape AABB_FLOOR_Z = Block.makeCuboidShape(6.0D, 0.0D, 5.0D, 10.0D, 8.0D, 11.0D);
    protected static final VoxelShape AABB_FLOOR_X = Block.makeCuboidShape(5.0D, 0.0D, 6.0D, 11.0D, 8.0D, 10.0D);
    protected static final VoxelShape AABB_NORTH = Block.makeCuboidShape(6.0D, 5.0D, 8.0D, 10.0D, 11.0D, 16.0D);
    protected static final VoxelShape AABB_SOUTH = Block.makeCuboidShape(6.0D, 5.0D, 0.0D, 10.0D, 11.0D, 8.0D);
    protected static final VoxelShape AABB_WEST = Block.makeCuboidShape(8.0D, 5.0D, 6.0D, 16.0D, 11.0D, 10.0D);
    protected static final VoxelShape AABB_EAST = Block.makeCuboidShape(0.0D, 5.0D, 6.0D, 8.0D, 11.0D, 10.0D);

    protected BlockBlastFurnacePipeGauge(Properties builder) {
        super(builder.setAllowsSpawn(Blocks::neverAllowSpawn).setOpaque(Blocks::isntSolid).setSuffocates(Blocks::isntSolid).setBlocksVision(Blocks::isntSolid));
        this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(FACE, AttachFace.WALL));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(HORIZONTAL_FACING);
        switch (state.get(FACE)) {
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
                    default:
                        return AABB_NORTH;
                }
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
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = getFacing(state).getOpposite();
        BlockPos blockpos = pos.offset(direction);
        BlockState blockState = worldIn.getBlockState(blockpos);
        return blockState.getBlock() == ModBlocks.BLAST_FURNACE_PIPE.get() || blockState.isSolidSide(worldIn, blockpos, direction.getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, FACE);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            RayTraceResult ray = mc.objectMouseOver;
            if (ray != null) {
                BlockPos pos = ray.getType() == RayTraceResult.Type.BLOCK ? new BlockPos(ray.getHitVec()) : null;
                BlockState state = pos != null ? Objects.requireNonNull(mc.world).getBlockState(pos) : null;
                Block block = state == null ? null : state.getBlock();
                if (block == ModBlocks.BLAST_FURNACE_PIPE_GAUGE.get()) {
                    Direction facing = state.get(HorizontalFaceBlock.FACE) != AttachFace.WALL
                            ? state.get(HorizontalFaceBlock.FACE) == AttachFace.CEILING
                            ? Direction.UP
                            : Direction.DOWN
                            : state.get(HorizontalFaceBlock.HORIZONTAL_FACING).getOpposite();
                    BlockPos offset = pos.offset(facing);
                    WorldHelper.getCapability(mc.world, offset, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()).ifPresent(handler -> {
                        int xc = event.getWindow().getScaledWidth() / 2;
                        int yc = event.getWindow().getScaledHeight() / 2;
                        int max = 0;
                        int amount = 0;

                        if (handler instanceof IFluidTank) {
                            max = ((IFluidTank) handler).getCapacity();
                            amount = ((IFluidTank) handler).getFluidAmount();
                        } else {
                            max = handler.getTankCapacity(0);
                            amount += handler.getFluidInTank(0).getAmount();
                        }

                        String s = new TranslationTextComponent(IPM.MOD_ID + ".ui.status.gauge").getString();
                        String s1 = amount + "/" + max;
                        mc.fontRenderer.drawStringWithShadow(event.getMatrixStack(), s, xc - mc.fontRenderer.getStringWidth(s) / 2F, yc + 10, 0xFFFFFF);
                        mc.fontRenderer.drawStringWithShadow(event.getMatrixStack(), s1, xc - mc.fontRenderer.getStringWidth(s) / 2F, yc + 20, 0xFFFFFF);
                    });
                }
            }
        }
    }
}
