package arekkuusu.implom.api.helper;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.nbt.IRedstoneNBTCapability;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Optional;

public class RedstoneHelper {

	public static Optional<IRedstoneNBTCapability> getCapability(ICapabilityProvider provider) {
		return getCapability(provider, null);
	}

	public static Optional<IRedstoneNBTCapability> getCapability(ICapabilityProvider provider, EnumFacing facing) {
		return provider.hasCapability(Capabilities.REDSTONE, facing)
				? Optional.ofNullable(provider.getCapability(Capabilities.REDSTONE, facing))
				: Optional.empty();
	}
}
