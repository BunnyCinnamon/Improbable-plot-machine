/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.worldaccess.data;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 30/10/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class WorldAccessTileWrapper<T extends TileEntity> implements IWorldAccess {

	private final T tile;
	private UUID key;

	public WorldAccessTileWrapper(T tile) {
		this.tile = tile;
	}

	public T getTile() {
		return tile;
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
