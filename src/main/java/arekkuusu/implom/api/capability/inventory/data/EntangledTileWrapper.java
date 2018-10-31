/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.inventory.data;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Improbable plot machine.
 * <p>
 * Default implementation for {@link TileEntity} with a quantum entangled inventory
 */
public class EntangledTileWrapper<T extends TileEntity> extends EntangledIItemWrapper {

	protected final T tile;
	private UUID key;

	/**
	 * @param tile  A {@link TileEntity} instance
	 * @param slots Slot amount
	 */
	public EntangledTileWrapper(T tile, int slots) {
		super(slots);
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
