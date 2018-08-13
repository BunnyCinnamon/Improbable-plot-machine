/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.energy;

/**
 * Created by <Arekkuusu> on 8/10/2018.
 * It's distributed as part of Solar.
 */
public interface IPholarized {

	default boolean canDrain() {
		return true;
	}

	default boolean canFill() {
		return true;
	}
}
