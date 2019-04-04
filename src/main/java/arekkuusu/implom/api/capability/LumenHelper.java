package arekkuusu.implom.api.capability;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Optional;

public final class LumenHelper {

	public static Optional<ILumenCapability> getCapability(ICapabilityProvider provider) {
		return getCapability(provider, null);
	}

	public static Optional<ILumenCapability> getCapability(ICapabilityProvider provider, EnumFacing facing) {
		return provider.hasCapability(Capabilities.LUMEN, facing)
				? Optional.ofNullable(provider.getCapability(Capabilities.LUMEN, facing))
				: Optional.empty();
	}

	public static void transfer(ILumenCapability from, ILumenCapability to, int amount, boolean exact) {
		if(from.get() > 0 && to.get() < to.getMax()) {
			int drained = from.drain(amount, false);
			int remain = to.fill(drained, false);
			if(drained > 0 && remain != amount && ((drained == amount && remain == 0) || !exact)) {
				from.drain(drained, true);
				to.fill(drained - remain, true);
			}
		}
	}
}
