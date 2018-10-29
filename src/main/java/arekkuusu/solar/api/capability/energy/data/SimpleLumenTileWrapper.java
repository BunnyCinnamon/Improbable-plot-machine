/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy.data;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 * <p>
 * Default implementation for {@link TileEntity} with a quantum entangled lumen storage
 */
public class SimpleLumenTileWrapper<T extends TileEntity> extends SimpleLumenWrapper {

	private final T tile;

	/**
	 * @param tile A {@link TileEntity} instance
	 * @param max  Lumen capacity
	 */
	public SimpleLumenTileWrapper(T tile, int max) {
		super(max);
		this.tile = tile;
	}

	@Override
	public void set(int neutrons) {
		super.set(neutrons);
		this.tile.markDirty();
	}
}
