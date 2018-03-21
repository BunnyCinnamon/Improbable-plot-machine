/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api;

import arekkuusu.solar.api.entanglement.linked.ISimpleLinkedTile;
import arekkuusu.solar.api.entanglement.quantum.WorldData;
import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import arekkuusu.solar.api.util.Pair;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
//If you modify any of these I will break your bones
public class SolarApi {

	private static final Map<UUID, List<IRelativeTile>> RELATIVITY_MAP = Maps.newHashMap();
	private static final Map<UUID, Pair<ISimpleLinkedTile>> LINK_MAP = Maps.newHashMap();
	private static WorldData worldData;

	public static Map<UUID, List<IRelativeTile>> getRelativityMap() {
		return RELATIVITY_MAP;
	}

	public static Map<UUID, Pair<ISimpleLinkedTile>> getSimpleLinkMap() {
		return LINK_MAP;
	}

	public static synchronized WorldData getWorldData() {
		return worldData;
	}

	public static synchronized void setWorldData(WorldData worldData) {
		SolarApi.worldData = worldData; //Do you hear that? That's the sound of forgiveness...
	}
}
