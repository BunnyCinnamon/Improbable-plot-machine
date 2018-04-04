/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.relativity;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.quantum.QuantumDataHandler;
import arekkuusu.solar.api.entanglement.quantum.data.PowerData;
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

	/**
	 * Checks if a {@param tile} is a relative list
	 *
	 * @param tile The {@link IRelativeTile} to be tested
	 * @param <T>  An impl of {@param tile}
	 * @return If the {@param tile} is relative to others
	 */
	public static <T extends TileEntity & IRelativeTile> boolean isRelative(T tile) {
		return tile.getKey().map(uuid -> SolarApi.getRelativityMap().containsKey(uuid)).orElse(false);
	}

	/**
	 * Add the given {@link IRelativeTile} to the relative list
	 *
	 * @param tile     The {@link TileEntity} to be added
	 * @param runnable If the {@param tile} is added, run {@param <T>}
	 * @param <T>      An impl of {@param tile}
	 */
	public static <T extends TileEntity & IRelativeTile> void addRelative(T tile, @Nullable Runnable runnable) {
		tile.getKey().ifPresent(uuid -> SolarApi.getRelativityMap().compute(uuid, (key, list) -> {
			list = list == null ? new ArrayList<>() : list;
			if(list.contains(tile)) return list;
			list.add(tile);
			if(runnable != null) {
				runnable.run();
			}
			return list;
		}));
	}

	/**
	 * Remove the given {@link IRelativeTile} from the relative list
	 *
	 * @param tile     The {@link TileEntity} to be removed
	 * @param runnable If the {@param tile} is removed, run {@param <T>}
	 * @param <T>      An impl of {@param tile}
	 */
	public static <T extends TileEntity & IRelativeTile> void removeRelative(T tile, @Nullable Runnable runnable) {
		tile.getKey().ifPresent(uuid -> SolarApi.getRelativityMap().compute(uuid, (key, list) -> {
			if(list != null) {
				list.remove(tile);
				if(runnable != null) {
					runnable.run();
				}
			}
			return list != null && !list.isEmpty() ? list : null;
		}));
	}

	/**
	 * Gets a list of relative tiles of the same {@param tile}
	 *
	 * @param tile The {@link TileEntity} relative
	 * @param <T>  An impl of {@param tile}
	 * @return The list
	 */
	public static <T extends TileEntity & IRelativeTile> List<IRelativeTile> getRelatives(T tile) {
		if(isRelative(tile)) {
			return Lists.newArrayList(SolarApi.getRelativityMap().get(tile.getKey().orElseThrow(NullPointerException::new))); //bamboozled
		}
		return Collections.emptyList();
	}

	/**
	 * Gets a list of relative tiles of the same {@param uuid}
	 *
	 * @param uuid The key
	 * @return The list
	 */
	public static List<IRelativeTile> getRelatives(UUID uuid) {
		if(SolarApi.getRelativityMap().containsKey(uuid)) {
			return Lists.newArrayList(SolarApi.getRelativityMap().get(uuid)); //bamboozled
		}
		return Collections.emptyList();
	}

	/**
	 * If the relative {@param tile} is powered by Redstone
	 *
	 * @param tile The relative tile {@link IRelativePower}
	 * @param <T>  An impl of {@param tile}
	 * @return If the redstone level is higher than 0
	 */
	public static <T extends TileEntity & IRelativePower> boolean isPowered(T tile) {
		return getPower(tile) > 0;
	}

	/**
	 * Get the Redstone power from the relative {@param tile}
	 *
	 * @param tile The relative tile {@link IRelativePower}
	 * @param <T>  An impl of {@param tile}
	 * @return The Redstone power from 0 to 15
	 */
	public static <T extends TileEntity & IRelativePower> int getPower(T tile) {
		return tile.getKey().map(uuid -> QuantumDataHandler.<PowerData>get(uuid)
				.map(PowerData::getI).orElse(0)
		).orElse(0);
	}

	/**
	 * Set the Redstone power to the relative {@param tile},
	 * this will update all other relative tiles, as long as
	 * they are loaded in the world
	 *
	 * @param tile     The relative tile {@link IRelativePower}
	 * @param newPower The new redstone power
	 * @param <T>      An impl of {@param tile}
	 */
	public static <T extends TileEntity & IRelativePower> void setPower(T tile, int newPower, boolean update) {
		tile.getKey().ifPresent(uuid -> QuantumDataHandler.getOrCreate(uuid, PowerData::new).setI(newPower));
		if(update) {
			updatePower(IRelativePower.class, tile);
		}
	}

	public static <T extends IRelativePower> void updatePower(Class<T> type, T tile) {
		tile.getKey().ifPresent(uuid -> {
			SolarApi.getRelativityMap().computeIfPresent(uuid, (k, list) -> {
				list.stream()
						.filter(t -> t.isLoaded() && t instanceof IRelativePower && type.isInstance(t))
						.map(t -> (IRelativePower) t).forEach(IRelativePower::onPowerUpdate);
				return list;
			});
		});
	}
}
