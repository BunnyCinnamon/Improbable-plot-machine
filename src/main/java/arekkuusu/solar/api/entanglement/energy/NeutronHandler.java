/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.energy;

import arekkuusu.solar.api.entanglement.quantum.QuantumDataHandler;
import arekkuusu.solar.api.entanglement.quantum.data.LumenData;

import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public final class NeutronHandler {

	public static int getNeutrons(UUID uuid) {
		return getNeutronData(uuid).get();
	}

	public static void setNeutrons(UUID uuid, int neutrons) {
		getNeutronData(uuid).set(neutrons);
	}

	public static LumenData getNeutronData(UUID uuid) {
		return QuantumDataHandler.getOrCreate(uuid, LumenData::new);
	}
}
