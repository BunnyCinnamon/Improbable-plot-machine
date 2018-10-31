package arekkuusu.solar.api.capability.worldaccess.data;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

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
