/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.energy.data;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 * <p>
 * Default quantum entangled {@link ILumen} implementation
 */
public abstract class ComplexLumenWrapper implements IComplexLumen {

	private int max;

	/**
	 * @param max Lumen capacity
	 */
	public ComplexLumenWrapper(int max) {
		this.max = max;
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
