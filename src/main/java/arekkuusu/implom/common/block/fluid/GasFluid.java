package arekkuusu.implom.common.block.fluid;

import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public abstract class GasFluid extends FlowingFluid {

    @Override
    public Fluid getFlowingFluid() {
        return ModFluids.FLOWING_HOT_AIR.get();
    }

    @Override
    public Fluid getStillFluid() {
        return ModFluids.HOT_AIR.get();
    }

    @Override
    public Item getFilledBucket() {
        return Items.WATER_BUCKET;
    }

    @Override
    protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld iWorld, BlockPos blockPos, BlockState blockState) {
        TileEntity tileentity = blockState.hasTileEntity() ? iWorld.getTileEntity(blockPos) : null;
        Block.spawnDrops(blockState, iWorld.getWorld(), blockPos, tileentity);
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader iWorldReader) {
        return 1;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader iWorldReader) {
        return 4;
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 0F;
    }

    @Override
    protected BlockState getBlockState(FluidState state) {
        return ModBlocks.HOT_AIR.get().getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    public static class Flowing extends GasFluid {
        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(FluidState p_207192_1_) {
            return p_207192_1_.get(LEVEL_1_8);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends GasFluid {
        public int getLevel(FluidState p_207192_1_) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
