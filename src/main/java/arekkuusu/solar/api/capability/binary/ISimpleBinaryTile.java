/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.binary;

import arekkuusu.solar.api.capability.quantum.IQuantum;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Solar.
 */
public interface ISimpleBinaryTile extends IQuantum {

	void add();

	void remove();

	Optional<ISimpleBinaryTile> getInverse();

	default boolean isLoaded() {
		return getRelativeWorld().isValid(getRelativePos()) && getRelativeWorld().isBlockLoaded(getRelativePos());
	}

	World getRelativeWorld();

	BlockPos getRelativePos();
}
