package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.common.handler.data.capability.FluidMultipleTank;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBlastFurnaceTuyere extends TileMultiblockImouto {

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
				? getTile(TileMultiblockOniichan.class, getWorld(), getOniichan()).map(tile -> tile.hasSecretCapability(capability, facing)).orElse(false)
				: super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && hasValidOniichan()
				? getTile(TileMultiblockOniichan.class, getWorld(), getOniichan()).map(tile -> tile.getSecretCapability(capability, facing))
				.map(c -> c instanceof FluidMultipleTank ? (T)((FluidMultipleTank) c).internal : c)
				.orElse(null)
				: super.getCapability(capability, facing);
	}
}
