/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.relativity.data;

import arekkuusu.implom.api.capability.quantum.IQuantum;
import arekkuusu.implom.api.capability.relativity.RelativityHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public interface IRelative extends IQuantum {
	String NBT_TAG = "relative_nbt";

	/**
	 * Default {@link IRelative} provider
	 */
	Callable<IRelative> DEFAULT = Empty::new;

	default List<IRelative> getAll() {
		return RelativityHandler.getRelatives(this);
	}

	/**
	 * Add this to the relative group
	 */
	default void add() {
		RelativityHandler.addRelative(this);
	}

	/**
	 * Remove this of the relative group
	 */
	default void remove() {
		RelativityHandler.removeRelative(this);
	}
}

class Empty implements IRelative {

	private UUID key;

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
	}
}