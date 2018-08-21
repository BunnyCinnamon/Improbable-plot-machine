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
import arekkuusu.solar.api.capability.quantum.data.PowerData;
import com.google.common.collect.Lists;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
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
	 * @param t   The {@param t} to be tested
	 * @param <T> An impl of {@param t}
	 * @return If the {@param t} is relative to others
	 */
	public static <T extends IRelativeState> boolean isRelative(T t) {
		return t.getKey().map(uuid -> SolarApi.getRelativityMap().containsKey(uuid)).orElse(false);
	}

	/**
	 * Add the given {@param t} to the relative list
	 *
	 * @param t        The {@link TileEntity} to be added
	 * @param runnable If the {@param t} is added, run {@param <T>}
	 * @param <T>      An impl of {@param t}
	 */
	public static <T extends IRelativeState> void addRelative(T t, @Nullable Runnable runnable) {
		t.getKey().ifPresent(uuid -> SolarApi.getRelativityMap().compute(uuid, (key, list) -> {
			list = list == null ? new ArrayList<>() : list;
			if(list.contains(t)) return list;
			list.add(t);
			if(runnable != null) {
				runnable.run();
			}
			return list;
		}));
	}

	/**
	 * Remove the given {@param t} from the relative list
	 *
	 * @param t        The {@link T} to be removed
	 * @param runnable If the {@param t} is removed, run {@param <T>}
	 * @param <T>      An impl of {@param t}
	 */
	public static <T extends IRelativeState> void removeRelative(T t, @Nullable Runnable runnable) {
		t.getKey().ifPresent(uuid -> SolarApi.getRelativityMap().compute(uuid, (key, list) -> {
			if(list != null) {
				list.remove(t);
				if(runnable != null) {
					runnable.run();
				}
			}
			return list != null && !list.isEmpty() ? list : null;
		}));
	}

	/**
	 * Gets a list of relative tiles of the same {@param t}
	 *
	 * @param t   The {@link T} relative
	 * @param <T> An impl of {@param t}
	 * @return The list
	 */
	public static <T extends IRelativeState> List<IRelativeState> getRelatives(T t) {
		if(isRelative(t)) {
			return Lists.newArrayList(SolarApi.getRelativityMap().get(t.getKey().orElseThrow(NullPointerException::new))); //bamboozled
		}
		return Collections.emptyList();
	}

	/**
	 * Gets a list of relative tiles of the same {@param uuid}
	 *
	 * @param uuid The key
	 * @return The list
	 */
	public static List<IRelativeState> getRelatives(UUID uuid) {
		if(SolarApi.getRelativityMap().containsKey(uuid)) {
			return Lists.newArrayList(SolarApi.getRelativityMap().get(uuid)); //bamboozled
		}
		return Collections.emptyList();
	}

	/**
	 * If the relative {@param t} is powered by Redstone
	 *
	 * @param t   The relative t {@link T}
	 * @param <T> An impl of {@param t}
	 * @return If the redstone level is higher than 0
	 */
	public static <T extends IRelativeRedstone> boolean isPowered(T t) {
		return getPower(t) > 0;
	}

	/**
	 * Get the Redstone power from the relative {@param t}
	 *
	 * @param t   The relative t {@link IRelativeRedstone}
	 * @param <T> An impl of {@param t}
	 * @return The Redstone power from 0 to 15
	 */
	public static <T extends IRelativeRedstone> int getPower(T t) {
		return t.getKey().map(uuid -> QuantumDataHandler.<PowerData>get(uuid)
				.map(PowerData::getI).orElse(0)
		).orElse(0);
	}

	/**
	 * Set the Redstone power to the relative {@param t},
	 * this will update all other relative tiles, as long as
	 * they are loaded in the world
	 *
	 * @param t        The relative t {@link IRelativeRedstone}
	 * @param newPower The new redstone power
	 * @param <T>      An impl of {@param t}
	 */
	public static <T extends IRelativeRedstone> void setPower(T t, int newPower, boolean update) {
		t.getKey().ifPresent(uuid -> QuantumDataHandler.getOrCreate(uuid, PowerData::new).setI(newPower));
		if(update) {
			updatePower(IRelativeRedstone.class, t);
		}
	}

	public static <T extends IRelativeRedstone> void updatePower(Class<T> type, T t) {
		t.getKey().ifPresent(uuid -> {
			SolarApi.getRelativityMap().computeIfPresent(uuid, (k, list) -> { list.stream()
					.filter(l -> l.isLoaded() && l instanceof IRelativeRedstone && type.isInstance(l))
					.map(l -> (IRelativeRedstone) l)
					.forEach(IRelativeRedstone::onPowerUpdate);
				return list;
			});
		});
	}
}
