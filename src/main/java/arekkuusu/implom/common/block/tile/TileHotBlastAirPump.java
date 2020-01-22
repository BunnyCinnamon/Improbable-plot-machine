package arekkuusu.implom.common.block.tile;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileHotBlastAirPump extends TileMultiblockImouto implements ITickable {

	private int tick;

	@Override
	public void update() {
		if(isClientWorld()) return;
		if(hasValidOniichan() && tick % 10 == 0) {
			getTile(TileHotBlastAirVent.class, getWorld(), getOniichan()).ifPresent(tileFrom -> {
				IFluidHandler handlerFrom = tileFrom.getSecretCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				assert handlerFrom != null; //Bruh

				getCapability(getWorld(), getPos().offset(getFacing()), getFacing().getOpposite(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(handlerTo -> {
					if(handlerFrom.getTankProperties()[0].getContents() == null) return;
					int filled = handlerTo.fill(handlerFrom.drain(Fluid.BUCKET_VOLUME, false), false);
					if(filled > 0) {
						FluidStack drained = handlerFrom.drain(filled, true);
						handlerTo.fill(drained, true);
					}
				});
			});
		}

		tick = (tick + 1) % 20;
	}

	public EnumFacing getFacing() {
		return getStateValue(BlockHorizontal.FACING, getPos()).orElse(EnumFacing.NORTH);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
				? getTile(TileMultiblockOniichan.class, getWorld(), getOniichan()).map(tile -> tile.hasSecretCapability(capability, facing)).orElse(false)
				: super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
				? getTile(TileMultiblockOniichan.class, getWorld(), getOniichan()).map(tile -> tile.getSecretCapability(capability, facing)).orElse(null)
				: super.getCapability(capability, facing);
	}
}
