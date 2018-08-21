/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.binary;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.util.Pair;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Solar.
 */
public final class BinaryHandler {

	/**
	 * If the given {@link @tile} of type {@link ISimpleBinaryTile}
	 * is linked to another tile in some other place
	 *
	 * @param tile The {@link ISimpleBinaryTile} to be tested
	 * @param <T>  An impl of {@param tile}
	 * @return If the {@param tile} is linked to another tile
	 */
	public static <T extends TileEntity & ISimpleBinaryTile> boolean hasBinary(T tile) {
		return tile.getKey().map(uuid -> SolarApi.getBinaryMap().containsKey(uuid)).orElse(false);
	}

	/**
	 * Add the given {@param tile} to a holder with its link
	 *
	 * @param tile The {@link ISimpleBinaryTile} to be added
	 * @param <T>  An impl of {@param tile}
	 */
	public static <T extends TileEntity & ISimpleBinaryTile> void add(T tile) {
		tile.getKey().ifPresent(uuid -> SolarApi.getBinaryMap().compute(uuid, (key, pair) -> {
			if(pair == null) pair = new Pair<>();
			if(pair.l == null || pair.r == null) {
				pair.offer(tile);
			}
			return pair;
		}));
	}

	/**
	 * Remove the given {@param tile} to a holder with its link
	 *
	 * @param tile The {@link ISimpleBinaryTile} to be removed
	 * @param <T>  An impl of {@param tile}
	 */
	public static <T extends TileEntity & ISimpleBinaryTile> void remove(T tile) {
		tile.getKey().ifPresent(uuid -> SolarApi.getBinaryMap().compute(uuid, (key, pair) -> {
			boolean present = false;
			if(pair != null) {
				present = pair.getInverse(tile) != null;
				pair.remove(tile);
			}
			return present ? pair : null;
		}));
	}

	/**
	 * Returns the inverse link of the given {@param tile}
	 *
	 * @param tile The {@link ISimpleBinaryTile}
	 * @param <T>  An impl of {@param tile}
	 * @return The inverse
	 */
	@Nullable
	public static <T extends TileEntity & ISimpleBinaryTile> ISimpleBinaryTile getInverse(T tile) {
		if(hasBinary(tile)) {
			return SolarApi.getBinaryMap().get(tile.getKey().orElseThrow(NullPointerException::new)).getInverse(tile); //bamboozled
		}
		return null;
	}

	/**
	 * Returns the pair of the given {@param uuid}
	 *
	 * @param uuid The key
	 * @return The pair
	 */
	public static Pair<ISimpleBinaryTile> getBinary(UUID uuid) {
		if(SolarApi.getBinaryMap().containsKey(uuid)) {
			return SolarApi.getBinaryMap().get(uuid);
		}
		return Pair.empty();
	}
}
