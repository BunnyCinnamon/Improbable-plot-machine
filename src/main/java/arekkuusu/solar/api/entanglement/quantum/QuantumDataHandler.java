/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.quantum.data.IQuantumData;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by <Snack> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public final class QuantumDataHandler {

	public static <S extends IQuantumData<?>> void add(UUID key, S t) {
		SolarApi.getDataMap().putIfAbsent(key, t);
		SolarApi.getQuantumData().markDirty();
	}

	public static void remove(UUID key) {
		SolarApi.getDataMap().remove(key);
		SolarApi.getQuantumData().markDirty();
	}

	@SuppressWarnings("unchecked")
	public static <S extends IQuantumData<?>> S getOrCreate(UUID key, Supplier<S> supplier) {
		return (S) get(key).orElseGet(() -> {
			S data = supplier.get();
			add(key, data);
			return data;
		});
	}

	@SuppressWarnings("unchecked")
	public static <S extends IQuantumData<?>> Optional<S> get(UUID key) {
		return Optional.ofNullable((S) SolarApi.getDataMap().get(key));
	}
}
