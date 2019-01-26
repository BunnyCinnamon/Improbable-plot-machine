/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.common.block.BlockSymmetricNegator.Constants;
import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Optional;

/**
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileSymmetricNegator extends TileBase implements ITickable {

	private int tick;

	@Override
	public void update() {
		if(!world.isRemote && tick++ % 2 == 0) {
			EnumFacing facing = getFacingLazy();
			findInterceptor(facing).ifPresent(reach -> {
				BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(getPos());
				BlockPos posInterceptor = pos.offset(facing, reach);
				int distance = 0;
				while(distance++ < reach) {
					getContainer(posOffset.move(facing), facing).ifPresent(container -> {
						getInterceptor(posInterceptor, facing).ifPresent(interceptor -> transferFluid(container, interceptor));
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

	private Optional<Integer> findInterceptor(EnumFacing facing) {
		BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(getPos());
		int distance = 0;
		while(distance++ < Constants.REACH) {
			IBlockState state = world.getBlockState(posOffset.move(facing));
			if(state.getBlock() == ModBlocks.SYMMETRIC_EXTENSION && state.getValue(BlockDirectional.FACING) == facing) {
				return Optional.of(distance);
			}
		}
		return Optional.empty();
	}

	private Optional<IFluidHandler> getInterceptor(BlockPos pos, EnumFacing facing) {
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == ModBlocks.SYMMETRIC_EXTENSION && state.getValue(BlockDirectional.FACING) == facing) {
			BlockPos offset = pos.offset(facing);
			return getContainer(offset, facing.getOpposite());
		}
		return Optional.empty();
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
