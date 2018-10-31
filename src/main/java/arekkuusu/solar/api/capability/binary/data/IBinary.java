package arekkuusu.solar.api.capability.binary.data;

import arekkuusu.solar.api.capability.binary.BinaryHandler;
import arekkuusu.solar.api.capability.quantum.IQuantum;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

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
