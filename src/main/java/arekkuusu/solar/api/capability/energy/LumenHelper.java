package arekkuusu.solar.api.capability.energy;

import arekkuusu.solar.api.capability.energy.data.ILumen;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public final class LumenHelper {

	public static Optional<ILumen> getCapability(ItemStack stack) {
		return stack.hasCapability(LumenStackProvider.NEUTRON_CAPABILITY, null)
				? Optional.ofNullable(stack.getCapability(LumenStackProvider.NEUTRON_CAPABILITY, null))
				: Optional.empty();
	}

	public static <T extends ILumen> Optional<T> getCapability(Class<T> cl, ItemStack stack) {
		return getCapability(stack).filter(cl::isInstance).map(cl::cast);
	}

	public static void transfer(ILumen from, ILumen to, int amount, boolean exact) {
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
