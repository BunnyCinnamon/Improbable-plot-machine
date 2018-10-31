/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api;

import arekkuusu.implom.api.capability.binary.data.IBinary;
import arekkuusu.implom.api.capability.quantum.WorldData;
import arekkuusu.implom.api.capability.relativity.data.IRelative;
import arekkuusu.implom.api.util.Pair;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
//If you modify any of these I will break your bones
public class IPMApi {

	private static final Map<UUID, List<IRelative>> RELATIVITY_MAP = Maps.newHashMap();
	private static final Map<UUID, Pair<IBinary>> BINARY_MAP = Maps.newHashMap();
	private static WorldData worldData;

	public static Map<UUID, List<IRelative>> getRelativityMap() {
		return RELATIVITY_MAP;
	}

	public static Map<UUID, Pair<IBinary>> getBinaryMap() {
		return BINARY_MAP;
	}

	public static synchronized WorldData getWorldData() {
		return worldData;
	}

	public static synchronized void setWorldData(WorldData worldData) {
		IPMApi.worldData = worldData; //Do you hear that? That's the sound of forgiveness...
	}
}
