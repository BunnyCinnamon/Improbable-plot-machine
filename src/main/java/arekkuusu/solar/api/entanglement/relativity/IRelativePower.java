/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.relativity;

import arekkuusu.solar.api.entanglement.IEntangledTile;

/**
 * Created by <Arekkuusu> on 11/12/2017.
 * It's distributed as part of Solar.
 */
public interface IRelativePower extends IEntangledTile {

	/**
	 * Updates this tile when the power amount changes
	 */
	void onPowerUpdate();
}
