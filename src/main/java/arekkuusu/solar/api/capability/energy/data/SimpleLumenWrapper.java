/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy.data;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 *
 * Default quantum entangled {@link ILumen} implementation
 */
public abstract class SimpleLumenWrapper implements ILumen {

	private int amount;
	private int max;

	/**
	 * @param max Lumen capacity
	 */
	public SimpleLumenWrapper(int max) {
		this.max = max;
	}

	@Override
	public int get() {
		return amount;
	}

	@Override
	public void set(int neutrons) {
		if(neutrons <= getMax()) {
			amount = neutrons;
		}
	}

	@Override
	public int getMax() {
		return max;
	}
}
