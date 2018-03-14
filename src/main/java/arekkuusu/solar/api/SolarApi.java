/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api;

import arekkuusu.solar.api.entanglement.linked.ISimpleLinkedTile;
import arekkuusu.solar.api.entanglement.quantum.data.IQuantumData;
import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import arekkuusu.solar.api.util.Pair;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class was created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
//If you modify any of these I will break your bones
public class SolarApi {

	private static final Map<UUID, List<IRelativeTile>> RELATIVITY_MAP = Maps.newHashMap();
	private static final Map<UUID, Pair<ISimpleLinkedTile>> LINKED_MAP = Maps.newHashMap();
	private static WorldQuantumData quantumData;

	public static Map<UUID, List<IRelativeTile>> getRelativityMap() {
		return RELATIVITY_MAP;
	}

	public static Map<UUID, Pair<ISimpleLinkedTile>> getSimpleLinkMap() {
		return LINKED_MAP;
	}

	public static Map<UUID, IQuantumData<?>> getDataMap() {
		return getQuantumData().DATA_MAP;
	}

	public static synchronized WorldQuantumData getQuantumData() {
		return quantumData;
	}

	public static synchronized void setQuantumData(WorldQuantumData quantumData) {
		SolarApi.quantumData = quantumData; //Do you hear that? That's the sound of forgiveness...
	}
}
