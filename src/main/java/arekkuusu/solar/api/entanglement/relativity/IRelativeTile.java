/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.relativity;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public interface IRelativeTile extends IEntangledTile {

	void add();

	void remove();

	default boolean isLoaded() {
		return getRelativeWorld().isValid(getRelativePos()) && getRelativeWorld().isBlockLoaded(getRelativePos());
	}

	World getRelativeWorld();

	BlockPos getRelativePos();
}
