/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.relativity;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class RelativityHandler {

	private static final Map<UUID, List<IRelativeTile>> RELATIVITY_MAP = new HashMap<>();

	public static boolean isRelative(IRelativeTile tile) {
		return RELATIVITY_MAP.containsKey(tile.getKey().orElse(null));
	}

	public static <T extends IRelativeTile> void addRelative(T tile, Consumer<T> consumer) {
		tile.getKey().ifPresent(uuid -> RELATIVITY_MAP.compute(uuid, (key, list) -> {
			list = list == null ? new ArrayList<>() : list;
			if(list.contains(tile)) return list;

			list.add(tile);
			consumer.accept(tile);

			return list;
		}));
	}

	public static <T extends IRelativeTile> void removeRelative(T tile, Consumer<T> consumer) {
		tile.getKey().ifPresent(uuid -> RELATIVITY_MAP.compute(uuid, (key, list) -> {
			if(list != null) {
				list.remove(tile);
				consumer.accept(tile);
			}

			return list != null && !list.isEmpty() ? list : null;
		}));
	}

	public static Map<UUID, List<IRelativeTile>> getRelativityMap() {
		return RELATIVITY_MAP;
	}
}
