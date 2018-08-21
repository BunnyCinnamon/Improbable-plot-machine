/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy.data;

import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public interface ILumen {
	String NBT_TAG = "lumen";

	/**
	 * Default {@link ILumen} provider
	 */
	Callable<ILumen> DEFAULT = Empty::new;

	/**
	 * Gets the current amount of lumen
	 *
	 * @return Lumen
	 */
	int get();

	/**
	 * Sets the current amount of lumen
	 *
	 * @param neutrons Lumen
	 */
	void set(int neutrons);

	/**
	 * Drains a certain amount of lumen
	 *
	 * @param amount Lumen to drain
	 * @return Lumen drained
	 */
	default int drain(int amount, boolean drain) {
		if(!canDrain()) return 0;
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

	/**
	 * Fills a certain amount of lumen
	 *
	 * @param amount Lumen to fill
	 * @return Lumen remain
	 */
	default int fill(int amount, boolean fill) {
		if(!canFill()) return amount;
		if(amount > 0) {
			int contained = get();
			if(contained >= getMax()) return amount;
			int sum = contained + amount;
			int remain = 0;
			if(fill) {
				set(sum);
			}
			return remain;
		} else return 0;
	}

	/**
	 * Maximum lumen capacity
	 *
	 * @return Lumen capacity
	 */
	int getMax();

	default boolean canDrain() {
		return true;
	}

	default boolean canFill() {
		return true;
	}

	/**
	 * Set maximum lumen capacity
	 *
	 * @param max Lumen capacity
	 */
	default void setMax(int max) { }
}

class Empty implements ILumen {

	private int max = Integer.MAX_VALUE;
	private int lumen;

	@Override
	public int get() {
		return lumen;
	}

	@Override
	public void set(int neutrons) {
		this.lumen = neutrons;
	}

	@Override
	public int getMax() {
		return max;
	}

	@Override
	public void setMax(int max) {
		this.max = max;
	}
}
