/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.relativity;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public interface IRelativeTile extends IEntangledTile {

	/**
	 * Add this tile entity to the relative group
	 */
	void add();

	/**
	 * Remove this tile entity of the relative group
	 */
	void remove();

	/**
	 * If the tile entity is in a loaded {@link BlockPos}
	 *
	 * @return If it is loaded
	 */
	default boolean isLoaded() {
		return getRelativeWorld().isValid(getRelativePos()) && getRelativeWorld().isBlockLoaded(getRelativePos());
	}

	/**
	 * Gets the world this tile entity is in
	 *
	 * @return The {@link World} instance
	 */
	World getRelativeWorld();

	/**
	 * Gets the position this tile entity in positioned in
	 *
	 * @return The {@link BlockPos} of this tile entity
	 */
	BlockPos getRelativePos();
}
