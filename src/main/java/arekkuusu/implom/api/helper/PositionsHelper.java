package arekkuusu.implom.api.helper;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Optional;

public final class PositionsHelper {

	public static Optional<IPositionsNBTDataCapability> getCapability(ICapabilityProvider provider) {
		return getCapability(provider, null);
	}

	public static Optional<IPositionsNBTDataCapability> getCapability(ICapabilityProvider provider, EnumFacing facing) {
		return provider.hasCapability(Capabilities.POSITIONS, facing)
				? Optional.ofNullable(provider.getCapability(Capabilities.POSITIONS, facing))
				: Optional.empty();
	}
}
