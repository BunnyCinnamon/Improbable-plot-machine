/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy.data;

import arekkuusu.solar.api.capability.energy.LumenHandler;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 * <p>
 * Default quantum entangled {@link ILumen} implementation
 */
public abstract class ComplexLumenWrapper implements ILumen {
	public static final String NBT_ENTANGLED_TAG = "entangled_lumen";

	private int max;

	/**
	 * @param max Lumen capacity
	 */
	public ComplexLumenWrapper(int max) {
		this.max = max;
	}

	@Override
	public int get() {
		return getKey().map(LumenHandler::getNeutrons).orElse(0);
	}

	@Override
	public void set(int neutrons) {
		if(neutrons <= getMax()) {
			getKey().ifPresent(uuid -> LumenHandler.setNeutrons(uuid, neutrons));
		}
	}

	@Override
	public int getMax() {
		return max;
	}

	@Override
	public int drain(int amount, boolean drain) {
		return getKey().map(uuid -> {
			if(amount > 0) {
				int contained = get();
				int drained = amount < getMax() ? amount : getMax();
				int remain = contained;
				int removed = remain < drained ? contained : drained;
				remain -= removed;
				if(drain) {
					set(remain);
				}
				return removed;
			} else return 0;
		}).orElse(0);
	}

	@Override
	public int fill(int amount, boolean fill) {
		return getKey().map(uuid -> {
			if(amount > 0) {
				int contained = get();
				if(contained >= getMax()) return amount;
				int sum = contained + amount;
				int remain = 0;
				if(sum > getMax()) {
					remain = sum - getMax();
					sum = getMax();
				}
				if(fill) {
					set(sum);
				}
				return remain;
			} else return 0;
		}).orElse(amount);
	}

	/**
	 * Gets the {@link UUID} of the lumen storage if it exists
	 *
	 * @return An {@link Optional<UUID>} containing the key
	 */
	public abstract Optional<UUID> getKey();

	/**
	 * Sets the {@link UUID} of the lumen storage
	 */
	public abstract void setKey(UUID key);
}
