/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.relativity;

import arekkuusu.solar.api.SolarApi;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class RelativityHandler {

	/**
	 * Checks if a {@param tile} is a relative list.
	 *
	 * @param tile The {@link IRelativeTile} in the shape of a {@link TileEntity}.
	 * @return If the {@param tile} is relative to others.
	 */
	public static <T extends TileEntity & IRelativeTile> boolean isRelative(T tile) {
		return tile.getKey().map(uuid -> SolarApi.getRelativityMap().containsKey(uuid)).orElse(false);
	}

	/**
	 * Add the given {@link IRelativeTile} to the relative list,
	 * MUST be implemented in a {@link TileEntity} and nothing else.
	 *
	 * @param tile The {@link TileEntity} to be added.
	 * @param runnable If the {@param tile} is added, run {@param <T>}.
	 * @param <T> An instance of {@param tile} that will be added.
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
	 * Remove the given {@link IRelativeTile} from the relative list,
	 * MUST be implemented in a {@link TileEntity} and nothing else.
	 *
	 * @param tile The {@link TileEntity} to be removed.
	 * @param runnable If the {@param tile} is removed, run {@param <T>}.
	 * @param <T> An instance of {@param tile} that will be removed.
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
}
