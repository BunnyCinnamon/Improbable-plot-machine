/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.energy.data;

import arekkuusu.implom.api.capability.energy.LumenHandler;
import arekkuusu.implom.api.capability.quantum.IQuantum;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 30/10/2018.
 * It's distributed as part of Improbable plot machine.
 */
public interface IComplexLumen extends ILumen, IQuantum {

	/**
	 * Default {@link ILumen} provider
	 */
	Callable<IComplexLumen> DEFAULT = EmptyComplex::new;

	@Override
	default int get() {
		return getKey().map(LumenHandler::getNeutrons).orElse(0);
	}

	@Override
	default void set(int neutrons) {
		if(neutrons <= getMax()) {
			getKey().ifPresent(uuid -> LumenHandler.setNeutrons(uuid, neutrons));
		}
	}

	@Override
	default int drain(int amount, boolean drain) {
		if(!getKey().isPresent()) return 0;
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
	}

	@Override
	default int fill(int amount, boolean fill) {
		if(!getKey().isPresent()) return amount;
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
		} else return amount;
	}
}

class EmptyComplex implements IComplexLumen {

	private UUID key;

	@Override
	public int getMax() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
	}
}