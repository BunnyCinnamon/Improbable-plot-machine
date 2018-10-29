/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.binary;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.capability.binary.data.IBinary;
import arekkuusu.solar.api.util.Pair;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Solar.
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
		return binary.getKey().map(uuid -> SolarApi.getBinaryMap().containsKey(uuid)).orElse(false);
	}

	/**
	 * Add the given {@param tile} to a holder with its link
	 *
	 * @param binary The {@link IBinary} to be added
	 * @param <T>    An impl of {@param binary}
	 */
	public static <T extends IBinary> void add(T binary) {
		binary.getKey().ifPresent(uuid -> SolarApi.getBinaryMap().compute(uuid, (key, pair) -> {
			if(pair == null) pair = new Pair<>();
			if(pair.l == null || pair.r == null) {
				pair.offer(binary);
			}
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
		binary.getKey().ifPresent(uuid -> SolarApi.getBinaryMap().compute(uuid, (key, pair) -> {
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
			return binary.getKey().map(key -> SolarApi.getBinaryMap().get(key)).map(pair -> pair.getInverse(binary));
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
		if(SolarApi.getBinaryMap().containsKey(uuid)) {
			return SolarApi.getBinaryMap().get(uuid);
		}
		return Pair.empty();
	}
}
