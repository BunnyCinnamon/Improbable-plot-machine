/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.quantum;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.api.capability.quantum.data.INBTData;
import arekkuusu.implom.common.IPM;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Improbable plot machine.
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
		WorldData data = IPMApi.getWorldData();
		data.saved.putIfAbsent(key, t);
		data.markDirty();
	}

	/**
	 * Removes a {@param t} NBT from its {@param key}
	 *
	 * @param key An {@link UUID}
	 */
	public static void remove(UUID key) {
		WorldData data = IPMApi.getWorldData();
		data.saved.remove(key);
		data.markDirty();
	}

	/**
	 * Gets the {@param t} NBT of a {@param key} if it exists or creates one from the {@param supplier}
	 *
	 * @param key An {@link UUID}
	 * @param cl  The {@param S} NBT class
	 * @param <S> The {@link INBTData} type
	 * @return the {@link INBTData} instance
	 */
	@SuppressWarnings("unchecked")
	public static <S extends INBTData<?>> S getOrCreate(Class<S> cl, UUID key) {
		return QuantumDataHandler.get(cl, key).orElseGet(() -> {
			S data = null;
			try {
				data = cl.newInstance();
				add(key, data);
			} catch (InstantiationException | IllegalAccessException e) {
				IPM.LOG.error("[QuantumDataHandler] - Class {} has no empty constructor", cl.getName());
				e.printStackTrace();
			}
			return data;
		});
	}

	/**
	 * Gets the {@param t} NBT of a {@param key} if it exists
	 *
	 * @param key An {@link UUID}
	 * @param cl  The {@param S} NBT class
	 * @param <S> The {@link INBTData} type
	 * @return the {@link Optional<INBTData>} object
	 */
	@SuppressWarnings("unchecked")
	public static <S extends INBTData<?>> Optional<S> get(Class<S> cl, UUID key) {
		return Optional.ofNullable(IPMApi.getWorldData().saved.get(key)).filter(cl::isInstance).map(cl::cast);
	}
}
