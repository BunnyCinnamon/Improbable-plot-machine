/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.binary;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.api.capability.binary.data.IBinary;
import arekkuusu.implom.api.util.Pair;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class BinaryHandler {

	/**
	 * If the given {@link @binary} of type {@link IBinary}
	 * is linked to another tile in some other place
	 *
	 * @param binary The {@link T} to be tested
	 * @param <T>    An impl of {@param binary}
	 * @return If the {@param tile} is linked to another tile
	 */
	public static <T extends IBinary> boolean hasBinary(T binary) {
		return binary.getKey().map(uuid -> IPMApi.getBinaryMap().containsKey(uuid)).orElse(false);
	}

	/**
	 * Add the given {@param tile} to a holder with its link
	 *
	 * @param binary The {@link IBinary} to be added
	 * @param <T>    An impl of {@param binary}
	 */
	public static <T extends IBinary> void add(T binary) {
		binary.getKey().ifPresent(uuid -> IPMApi.getBinaryMap().compute(uuid, (key, pair) -> {
			if(pair == null) pair = new Pair<>();
			pair.offer(binary);
			return pair;
		}));
	}

	/**
	 * Remove the given {@param binary} to a holder with its link
	 *
	 * @param binary The {@link IBinary} to be removed
	 * @param <T>    An impl of {@param binary}
	 */
	public static <T extends IBinary> void remove(T binary) {
		binary.getKey().ifPresent(uuid -> IPMApi.getBinaryMap().compute(uuid, (key, pair) -> {
			boolean present = false;
			if(pair != null) {
				present = pair.getInverse(binary) != null;
				pair.remove(binary);
			}
			return present ? pair : null;
		}));
	}

	/**
	 * Returns the inverse link of the given {@param binary}
	 *
	 * @param binary The {@link IBinary}
	 * @param <T>    An impl of {@param binary}
	 * @return The inverse
	 */
	@Nullable
	public static <T extends IBinary> Optional<IBinary> getInverse(T binary) {
		if(hasBinary(binary)) {
			return binary.getKey().map(key -> IPMApi.getBinaryMap().get(key)).map(pair -> pair.getInverse(binary));
		}
		return Optional.empty();
	}

	/**
	 * Returns the pair of the given {@param uuid}
	 *
	 * @param uuid The key
	 * @return The pair
	 */
	public static Pair<IBinary> getBinaryPair(UUID uuid) {
		if(IPMApi.getBinaryMap().containsKey(uuid)) {
			return IPMApi.getBinaryMap().get(uuid);
		}
		return Pair.empty();
	}
}
