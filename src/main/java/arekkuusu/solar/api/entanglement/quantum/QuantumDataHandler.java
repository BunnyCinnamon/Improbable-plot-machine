/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.quantum.data.INBTData;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by <Snack> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public final class QuantumDataHandler {

	public static <S extends INBTData<?>> void add(UUID key, S t) {
		WorldData data = SolarApi.getWorldData();
		data.saved.putIfAbsent(key, t);
		data.markDirty();
	}

	public static void remove(UUID key) {
		WorldData data = SolarApi.getWorldData();
		data.saved.remove(key);
		data.markDirty();
	}

	@SuppressWarnings("unchecked")
	public static <S extends INBTData<?>> S getOrCreate(UUID key, Supplier<S> supplier) {
		return (S) get(key).orElseGet(() -> {
			S data = supplier.get();
			add(key, data);
			return data;
		});
	}

	@SuppressWarnings("unchecked")
	public static <S extends INBTData<?>> Optional<S> get(UUID key) {
		return Optional.ofNullable((S) SolarApi.getWorldData().saved.get(key));
	}
}
