/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.neutron.data;

import arekkuusu.solar.api.entanglement.neutron.NeutronHandler;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public abstract class NeutronWrapper implements INeutron {

	private int max;

	public NeutronWrapper(int max) {
		this.max = max;
	}

	@Override
	public int get() {
		return getKey().map(NeutronHandler::getNeutrons).orElse(0);
	}

	@Override
	public void set(int neutrons) {
		getKey().ifPresent(uuid -> NeutronHandler.setNeutrons(uuid, neutrons));
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public abstract Optional<UUID> getKey();

	public int drain(int amount) {
		return getKey().map(uuid -> {
			if(amount > 0) {
				int contained = get();
				int drained = amount < getMax() ? amount : getMax();
				int remain = contained;
				int removed = remain < drained ? contained : drained;
				remain -= removed;
				set(remain);
				return removed;
			} else return 0;
		}).orElse(0);
	}

	public int fill(int amount) {
		return getKey().map(uuid -> {
			if(amount > 0) {
				int contained = get();
				int sum = contained + amount;
				int remain = 0;
				if(sum > getMax()) {
					remain = sum - getMax();
					sum = getMax();
				}
				set(sum);
				return remain;
			} else return 0;
		}).orElse(amount);
	}
}
