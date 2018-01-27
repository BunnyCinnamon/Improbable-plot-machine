/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import arekkuusu.solar.api.state.State;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;

/**
 * Created by <Snack> on 23/01/2018.
 * It's distributed as part of Solar.
 */
public class TileAlternator extends TileRelativeBase {

	private static final Map<UUID, Integer> ACTIVE_MAP = Maps.newHashMap();

	public boolean areAllActive() {
		return getKey().map(key -> {
			int size = Math.toIntExact(RelativityHandler.getRelatives(key).stream().filter(IRelativeTile::isLoaded).count());
			int loaded = ACTIVE_MAP.get(key);
			return size == loaded;
		}).orElse(false);
	}

	public boolean isActiveLazy() {
		return getStateValue(State.ACTIVE, pos).orElse(false);
	}

	@Override
	public void add() {
		if(!world.isRemote) {
			RelativityHandler.addRelative(this, () -> {
				getKey().ifPresent(key -> {
					ACTIVE_MAP.put(key, ACTIVE_MAP.getOrDefault(key, 0) + 1);
				});
			});
		}
	}

	@Override
	public void remove() {
		if(!world.isRemote) {
			RelativityHandler.removeRelative(this, () -> {
				getKey().ifPresent(key -> {
					ACTIVE_MAP.put(key, ACTIVE_MAP.getOrDefault(key, 0) - 1);
				});
			});
		}
	}
}
