/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.block.fluid.ModFluids;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Solar.
 */
public class TileDifferentiator extends TileBase implements ITickable {

	public static final int REACH = 16;
	private int tick;

	@Override
	public void update() {
		if(!world.isRemote && tick++ % 2 == 0) {
			EnumFacing facing = getFacingLazy();
			findInterceptor(facing).ifPresent(reach -> {
				BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(getPos());
				BlockPos posInterceptor = pos.offset(facing, reach);
				IFluidHandler handler = getInterceptor(posInterceptor, facing);
				int distance = 0;
				while(distance++ < reach) {
					getContainer(posOffset.move(facing), facing).ifPresent(container -> {
						if(handler != null) {
							transferFluid(container, handler);
						} else {
							transferLumen(container, posInterceptor, facing);
						}
					});
				}
			});
		}
	}

	private Optional<IFluidHandler> getContainer(BlockPos pos, EnumFacing facing) {
		return getCapability(world, pos, facing, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
	}

	private void transferFluid(IFluidHandler from, IFluidHandler to) {
		FluidStack testDrain = from.drain(1, false);
		int fill = to.fill(testDrain, false);
		if(fill > 0) {
			to.fill(from.drain(1, true), true);
		}
	}

	private void transferLumen(IFluidHandler from, BlockPos to, EnumFacing facing) {
		FluidStack gold = new FluidStack(ModFluids.GOLD, Fluid.BUCKET_VOLUME);
		FluidStack testDrain = from.drain(gold, false);
		if(testDrain != null && testDrain.amount == Fluid.BUCKET_VOLUME) {
			getCapability(world, to.offset(facing), facing.getOpposite(), ModCapability.NEUTRON_CAPABILITY).ifPresent(lumen -> {
				if(lumen.fill(1, false) == 0) {
					from.drain(gold, true);
					lumen.fill(1, true);
				}
			});
		}
	}

	private Optional<Integer> findInterceptor(EnumFacing facing) {
		BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(getPos());
		int distance = 0;
		while(distance++ < REACH) {
			IBlockState state = world.getBlockState(posOffset.move(facing));
			if(state.getBlock() == ModBlocks.DIFFERENTIATOR_INTERCEPTOR && state.getValue(BlockDirectional.FACING) == facing) {
				return Optional.of(distance);
			}
		}
		return Optional.empty();
	}

	@Nullable
	private IFluidHandler getInterceptor(BlockPos pos, EnumFacing facing) {
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == ModBlocks.DIFFERENTIATOR_INTERCEPTOR && state.getValue(BlockDirectional.FACING) == facing) {
			BlockPos offset = pos.offset(facing);
			return getContainer(offset, facing.getOpposite()).orElse(null);
		}
		return null;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
