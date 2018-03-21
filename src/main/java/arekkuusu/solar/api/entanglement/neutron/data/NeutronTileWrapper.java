/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.neutron.data;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import net.minecraft.tileentity.TileEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public class NeutronTileWrapper<T extends TileEntity & IEntangledTile> extends NeutronWrapper {

	private T tile;

	public NeutronTileWrapper(T tile, int max) {
		super(max);
		this.tile = tile;
	}

	@Override
	public Optional<UUID> getKey() {
		return tile.getKey();
	}
}