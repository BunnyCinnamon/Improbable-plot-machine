/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.binary.data;

import arekkuusu.implom.api.capability.binary.BinaryHandler;
import arekkuusu.implom.api.capability.quantum.IQuantum;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public interface IBinary extends IQuantum {
	String NBT_TAG = "binary_nbt";

	/**
	 * Default {@link IBinary} provider
	 */
	Callable<IBinary> DEFAULT = Empty::new;

	default void add() {
		BinaryHandler.add(this);
	}

	default void remove() {
		BinaryHandler.remove(this);
	}

	default Optional<IBinary> getInverse() {
		return BinaryHandler.getInverse(this);
	}
}

class Empty implements IBinary {

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
