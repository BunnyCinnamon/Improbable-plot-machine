/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.relativity;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.capability.quantum.QuantumDataHandler;
import arekkuusu.solar.api.capability.quantum.data.RedstoneData;
import arekkuusu.solar.api.capability.relativity.data.IRelative;
import arekkuusu.solar.api.capability.relativity.data.IRelativeRedstone;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public final class RelativityHandler {
	public static final String NBT_TAG = "entangled_relativity";

	/**
	 * Checks if a {@param t} is a relative list
	 *
	 * @param relative The {@param relative} to be tested
	 * @param <R>      An impl of {@link R}
	 * @return If the {@param t} is relative to others
	 */
	public static <R extends IRelative> boolean isRelative(R relative) {
		return relative.getKey()
				.map(uuid -> SolarApi.getRelativityMap().containsKey(uuid))
				.orElse(false);
	}

	/**
	 * Add the given {@param relative} to the relative list
	 *
	 * @param relative The {@link R} to be added
	 * @param <R>      An impl of {@param relative}
	 */
	public static <R extends IRelative> boolean addRelative(R relative) {
		boolean[] updated = {false};
		relative.getKey().ifPresent(uuid -> SolarApi.getRelativityMap().compute(uuid, (key, list) -> {
			list = list == null ? new ArrayList<>() : list;
			if(list.contains(relative)) return list;
			list.add(relative);
			updated[0] = true;
			return list;
		}));
		return updated[0];
	}

	/**
	 * Remove the given {@param relative} from the relative list
	 *
	 * @param relative The {@link R} to be removed
	 * @param <R>      An impl of {@param t}
	 */
	public static <R extends IRelative> boolean removeRelative(R relative) {
		boolean[] updated = {false};
		relative.getKey().ifPresent(uuid -> SolarApi.getRelativityMap().compute(uuid, (key, list) -> {
			if(list != null) {
				list.remove(relative);
				updated[0] = true;
			}
			return list != null && !list.isEmpty() ? list : null;
		}));
		return updated[0];
	}

	/**
	 * Gets a list of relative tiles of the same {@param relative}
	 *
	 * @param relative The {@link IRelative} relative
	 * @return The list
	 */
	public static List<IRelative> getRelatives(IRelative relative) {
		if(isRelative(relative)) {
			return relative.getKey()
					.map(key -> Lists.newArrayList(SolarApi.getRelativityMap().get(key)))
					.orElse(Lists.newArrayList());
		}
		return Collections.emptyList();
	}

	/**
	 * Gets a list of relative tiles of the same {@param uuid}
	 *
	 * @param uuid The key
	 * @return The list
	 */
	public static List<IRelative> getRelatives(UUID uuid) {
		if(SolarApi.getRelativityMap().containsKey(uuid)) {
			return Lists.newArrayList(SolarApi.getRelativityMap().get(uuid)); //bamboozled
		}
		return Collections.emptyList();
	}

	/**
	 * If the relative {@param relative} is powered by Redstone
	 *
	 * @param relative The relative relative {@link R}
	 * @param <R>      An impl of {@param relative}
	 * @return If the redstone level is higher than 0
	 */
	public static <R extends IRelativeRedstone> boolean isPowered(R relative) {
		return getPower(relative) > 0;
	}

	/**
	 * Get the Redstone power from the relative {@param relative}
	 *
	 * @param relative The relative relative {@link R}
	 * @param <R>      An impl of {@param relative}
	 * @return The Redstone power from 0 to 15
	 */
	public static <R extends IRelativeRedstone> int getPower(R relative) {
		return relative.getKey()
				.map(uuid -> QuantumDataHandler.get(RedstoneData.class, uuid).map(RedstoneData::get).orElse(0))
				.orElse(0);
	}

	/**
	 * Set the Redstone power to the relative {@param t},
	 * this will update all other relative tiles, as long as
	 * they are loaded in the world
	 *
	 * @param relative The relative relative {@link R}
	 * @param power    The new redstone power
	 * @param <R>      An impl of {@param relative}
	 */
	public static <R extends IRelativeRedstone> void setPower(R relative, int power, boolean update) {
		relative.getKey().ifPresent(uuid -> {
			RedstoneData data = QuantumDataHandler.getOrCreate(RedstoneData.class, uuid);
			data.set(power);
			if(update) {
				SolarApi.getRelativityMap().computeIfPresent(uuid, (k, list) -> {
					list.stream()
							.filter(l -> l instanceof IRelativeRedstone)
							.map(l -> (IRelativeRedstone) l)
							.forEach(IRelativeRedstone::onPowerUpdate);
					return list;
				});
			}
		});
	}
}
