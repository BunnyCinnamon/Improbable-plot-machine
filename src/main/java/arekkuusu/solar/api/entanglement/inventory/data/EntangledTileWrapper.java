/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.inventory.data;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import net.minecraft.tileentity.TileEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 *
 * Default implementation for {@link TileEntity} with a quantum entangled inventory
 */
public class EntangledTileWrapper<T extends TileEntity & IEntangledTile> extends EntangledIItemWrapper {

	protected final T tile;

	/**
	 * @param tile   A {@link TileEntity} instance implementing {@link T}
	 * @param slots  Slot amount
	 */
	public EntangledTileWrapper(T tile, int slots) {
		super(slots);
		this.tile = tile;
	}

	@Override
	public Optional<UUID> getKey() {
		return tile.getKey();
	}
}
