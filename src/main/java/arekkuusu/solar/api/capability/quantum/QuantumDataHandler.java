/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.quantum;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.capability.quantum.data.INBTData;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public final class QuantumDataHandler {

	/**
	 * Adds a {@param t} NBT to a {@param key} if it is absent
	 *
	 * @param key An {@link UUID}
	 * @param t   An {@link S} NBT
	 * @param <S> The {@param t} NBT inserted
	 */
	public static <S extends INBTData<?>> void add(UUID key, S t) {
		WorldData data = SolarApi.getWorldData();
		data.saved.putIfAbsent(key, t);
		data.markDirty();
	}

	/**
	 * Removes a {@param t} NBT from its {@param key}
	 *
	 * @param key An {@link UUID}
	 */
	public static void remove(UUID key) {
		WorldData data = SolarApi.getWorldData();
		data.saved.remove(key);
		data.markDirty();
	}

	/**
	 * Gets the {@param t} NBT of a {@param key} if it exists or creates one from the {@param supplier}
	 *
	 * @param key      An {@link UUID}
	 * @param supplier A {@link Supplier<S>} for an {@param S} NBT
	 * @param <S>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <S extends INBTData<?>> S getOrCreate(UUID key, Supplier<S> supplier) {
		return (S) get(key).orElseGet(() -> {
			S data = supplier.get();
			add(key, data);
			return data;
		});
	}

	/**
	 * Gets the {@param t} NBT of a {@param key} if it exists
	 *
	 * @param key An {@link UUID}
	 * @param <S> The {@link INBTData} type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <S extends INBTData<?>> Optional<S> get(UUID key) {
		return Optional.ofNullable((S) SolarApi.getWorldData().saved.get(key));
	}
}
