package arekkuusu.solar.api.capability.binary.data;

import arekkuusu.solar.api.capability.binary.BinaryHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class BinaryTileWrapper<T extends TileEntity> implements IBinary {

	private final T tile;
	private UUID key;

	public BinaryTileWrapper(T tile) {
		this.tile = tile;
	}

	public void add() {
		if(!getWorld().isRemote) {
			BinaryHandler.add(this);
		}
	}

	public void remove() {
		if(!getWorld().isRemote) {
			BinaryHandler.remove(this);
		}
	}

	public World getWorld() {
		return this.tile.getWorld();
	}

	public BlockPos getPos() {
		return this.tile.getPos();
	}

	public boolean isLoaded() {
		return getWorld().isValid(getPos()) && getWorld().isBlockLoaded(getPos());
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		if(getWorld() != null) this.remove();
		this.key = key;
		if(getWorld() != null) this.add();
		this.tile.markDirty();
	}
}
