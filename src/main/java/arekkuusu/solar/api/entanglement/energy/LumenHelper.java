package arekkuusu.solar.api.entanglement.energy;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;

public final class LumenHelper {

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
