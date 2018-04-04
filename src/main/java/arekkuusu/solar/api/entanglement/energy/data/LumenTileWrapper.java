/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.energy.data;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import net.minecraft.tileentity.TileEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 *
 * Default implementation for {@link TileEntity} with a quantum entangled lumen storage
 */
public class LumenTileWrapper<T extends TileEntity & IEntangledTile> extends LumenWrapper {

	private T tile;

	/**
	 * @param tile A {@link TileEntity} instance implementing {@link T}
	 * @param max  Lumen capacity
	 */
	public LumenTileWrapper(T tile, int max) {
		super(max);
		this.tile = tile;
	}

	@Override
	public Optional<UUID> getKey() {
		return tile.getKey();
	}
}
