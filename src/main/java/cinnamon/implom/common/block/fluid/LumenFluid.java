package cinnamon.implom.common.block.fluid;

import cinnamon.implom.IPM;
import cinnamon.implom.common.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class LumenFluid extends FlowingFluid {

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_LUMEN.get();
    }

    @Override
    public Fluid getSource() {
        return ModFluids.FLOWING_LUMEN.get();
    }

    @Override
    public Item getBucket() {
        return Items.WATER_BUCKET;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState arg, BlockGetter arg2, BlockPos arg3, Fluid arg4, Direction arg5) {
        return arg5 == Direction.UP && !arg4.is(FluidTags.WATER);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor accessor, BlockPos arg2, BlockState arg3) {
        BlockEntity tileentity = arg3.hasBlockEntity() ? accessor.getBlockEntity(arg2) : null;
        Block.dropResources(arg3, accessor, arg2, tileentity);
    }

    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    @Override
    protected int getSlopeFindDistance(LevelReader arg) {
        return 1;
    }


    @Override
    protected int getDropOff(LevelReader arg) {
        return 4;
    }

    @Override
    public int getTickDelay(LevelReader arg) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 0F;
    }

    @Override
    protected FluidAttributes createAttributes() {
        return FluidAttributes.builder(
                        new ResourceLocation(IPM.MOD_ID, "block/lumen_still"),
                        new ResourceLocation(IPM.MOD_ID, "block/lumen_flow"))
                .translationKey("block." + IPM.MOD_ID + ".lumen")
                .luminosity(0).density(100).viscosity(500).temperature(1300).build(this);
    }

    @Override
    public BlockState createLegacyBlock(FluidState arg) {
        return ModBlocks.LUMEN.get().defaultBlockState().setValue(LEVEL, getLegacyLevel(arg));
    }

    public static class Flowing extends LumenFluid {
        public Flowing() {
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> arg) {
            super.createFluidStateDefinition(arg);
            arg.add(LEVEL);
        }

        public int getAmount(FluidState arg) {
            return arg.getValue(LEVEL);
        }

        public boolean isSource(FluidState arg) {
            return false;
        }
    }

    public static class Source extends LumenFluid {
        public Source() {
        }

        public int getAmount(FluidState arg) {
            return 8;
        }

        public boolean isSource(FluidState arg) {
            return true;
        }
    }
}
