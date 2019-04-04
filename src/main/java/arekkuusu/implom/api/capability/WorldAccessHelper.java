package arekkuusu.implom.api.capability;

import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Optional;

public final class WorldAccessHelper {

	public static Optional<IWorldAccessNBTDataCapability> getCapability(ICapabilityProvider provider) {
		return getCapability(provider, null);
	}

	public static Optional<IWorldAccessNBTDataCapability> getCapability(ICapabilityProvider provider, EnumFacing facing) {
		return provider.hasCapability(Capabilities.WORLD_ACCESS, facing)
				? Optional.ofNullable(provider.getCapability(Capabilities.WORLD_ACCESS, facing))
				: Optional.empty();
	}
}
