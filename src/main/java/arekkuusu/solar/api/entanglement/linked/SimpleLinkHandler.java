/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.linked;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.util.Pair;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Snack> on 20/01/2018.
 * It's distributed as part of Solar.
 */
public final class SimpleLinkHandler {

	/**
	 * If the given {@link @tile} of type {@link ISimpleLinkedTile}
	 * is linked to another tile in some other place.
	 *
	 * @param tile The {@link ISimpleLinkedTile} to be tested.
	 * @param <T>  An impl of {@param tile}.
	 * @return If the {@param tile} is linked to another tile.
	 */
	public static <T extends TileEntity & ISimpleLinkedTile> boolean isLinked(T tile) {
		return tile.getKey().map(uuid -> SolarApi.getSimpleLinkMap().containsKey(uuid)).orElse(false);
	}

	/**
	 * Add the given {@param tile} to a holder with its link.
	 *
	 * @param tile The {@link ISimpleLinkedTile} to be added.
	 * @param <T>  An impl of {@param tile}.
	 */
	public static <T extends TileEntity & ISimpleLinkedTile> void addLink(T tile) {
		tile.getKey().ifPresent(uuid -> SolarApi.getSimpleLinkMap().compute(uuid, (key, pair) -> {
			if(pair == null) pair = new Pair<>();
			if(pair.l == null || pair.r == null) {
				pair.offer(tile);
			}
			return pair;
		}));
	}

	/**
	 * Remove the given {@param tile} to a holder with its link.
	 *
	 * @param tile The {@link ISimpleLinkedTile} to be removed.
	 * @param <T>  An impl of {@param tile}.
	 */
	public static <T extends TileEntity & ISimpleLinkedTile> void removeLink(T tile) {
		tile.getKey().ifPresent(uuid -> SolarApi.getSimpleLinkMap().compute(uuid, (key, pair) -> {
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
	 * @param tile The {@link ISimpleLinkedTile}.
	 * @param <T>  An impl of {@param tile}.
	 * @return The inverse.
	 */
	@Nullable
	public static <T extends TileEntity & ISimpleLinkedTile> ISimpleLinkedTile getInverseLink(T tile) {
		if(isLinked(tile)) {
			return SolarApi.getSimpleLinkMap().get(tile.getKey().orElseThrow(NullPointerException::new)).getInverse(tile); //bamboozled
		}
		return null;
	}

	/**
	 * Returns the pair of the given {@param uuid}
	 *
	 * @param uuid The key.
	 * @return The pair.
	 */
	public static Pair<ISimpleLinkedTile> getPair(UUID uuid) {
		if(SolarApi.getSimpleLinkMap().containsKey(uuid)) {
			return SolarApi.getSimpleLinkMap().get(uuid);
		}
		return Pair.empty();
	}
}
