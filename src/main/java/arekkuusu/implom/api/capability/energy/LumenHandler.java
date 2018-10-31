/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.energy;

import arekkuusu.implom.api.capability.quantum.QuantumDataHandler;
import arekkuusu.implom.api.capability.quantum.data.LumenData;

import java.util.UUID;

/*
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class LumenHandler {

	/**
	 * Gets the current amount of lumen in a quantum storage
	 *
	 * @param uuid the {@link UUID} of the lumen storage
	 * @return lumen
	 */
	public static int getNeutrons(UUID uuid) {
		return getNeutronData(uuid).get();
	}

	/**
	 * Sets the current amount of lumen in a quantum storage
	 *
	 * @param uuid     the {@link UUID} of the lumen storage
	 * @param neutrons lumen
	 */
	public static void setNeutrons(UUID uuid, int neutrons) {
		getNeutronData(uuid).set(neutrons);
	}

	/**
	 * Gets the {@link LumenData} of this lumen storage
	 *
	 * @param uuid the {@link UUID} of the lumen storage
	 * @return the quantum entangled lumen storage
	 */
	public static LumenData getNeutronData(UUID uuid) {
		return QuantumDataHandler.getOrCreate(LumenData.class, uuid);
	}
}
