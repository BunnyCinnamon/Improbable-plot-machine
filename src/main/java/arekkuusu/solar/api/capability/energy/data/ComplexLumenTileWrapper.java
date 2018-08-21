/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy.data;

import arekkuusu.solar.api.capability.quantum.IQuantum;
import net.minecraft.tileentity.TileEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 * <p>
 * Default implementation for {@link TileEntity} with a quantum entangled lumen storage
 */
public class ComplexLumenTileWrapper<T extends TileEntity & IQuantum> extends ComplexLumenWrapper {

	private T tile;

	/**
	 * @param tile A {@link TileEntity} instance implementing {@link T}
	 * @param max  Lumen capacity
	 */
	public ComplexLumenTileWrapper(T tile, int max) {
		super(max);
		this.tile = tile;
	}

	public T getTile() {
		return tile;
	}

	@Override
	public Optional<UUID> getKey() {
		return tile.getKey();
	}

	@Override
	public void setKey(UUID key) {
		tile.setKey(key);
	}
}
