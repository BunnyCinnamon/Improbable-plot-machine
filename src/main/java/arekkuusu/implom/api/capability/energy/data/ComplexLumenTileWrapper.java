/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.energy.data;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 * <p>
 * Default implementation for {@link TileEntity} with a quantum entangled lumen storage
 */
public class ComplexLumenTileWrapper<T extends TileEntity> extends ComplexLumenWrapper {

	private final T tile;
	private UUID key;

	/**
	 * @param tile A {@link TileEntity} instance implementing {@link T}
	 * @param max  Lumen capacity
	 */
	public ComplexLumenTileWrapper(T tile, int max) {
		super(max);
		this.tile = tile;
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
		this.tile.markDirty();
	}
}
