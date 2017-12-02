/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.entanglement.quantum.IQuantumTile;
import net.minecraft.tileentity.TileEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public class QuantumTileWrapper<T extends TileEntity & IQuantumTile> extends QuantumDataHandler {

	protected final T tile;

	public QuantumTileWrapper(T tile, int slots) {
		super(slots);
		this.tile = tile;
	}

	@Override
	public Optional<UUID> getKey() {
		return tile.getKey();
	}

	@Override
	protected void onChange(int slot) {
		WorldQuantumData.get(tile.getWorld()).markDirty();
	}
}
