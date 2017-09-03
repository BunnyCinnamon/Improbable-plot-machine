/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.quantum.IEntangledTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public class QuantumTileWrapper<T extends TileEntity & IEntangledTile<T>> extends QuantumHandler {

	private final T tile;

	public QuantumTileWrapper(T tile) {
		super(1);
		this.tile = tile;
	}

	@Nullable
	@Override
	public UUID getKey() {
		return tile.getKey();
	}

	@Override
	protected void onChange(int slot) {
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		tile.getWorld().notifyNeighborsOfStateChange(tile.getPos(), state.getBlock(), true);

		WorldQuantumData.get(tile.getWorld()).markDirty();
	}
}
