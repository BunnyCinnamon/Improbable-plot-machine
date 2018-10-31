package arekkuusu.implom.api.capability.relativity.data;

import arekkuusu.implom.api.capability.relativity.RelativityHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class RelativeTileWrapper<T extends TileEntity> implements IRelative {

	private final T tile;
	private UUID key;

	public RelativeTileWrapper(T tile) {
		this.tile = tile;
	}

	public T getTile() {
		return tile;
	}

	@Override
	public void add() {
		if(!getWorld().isRemote) {
			RelativityHandler.addRelative(this);
		}
	}

	@Override
	public void remove() {
		if(!getWorld().isRemote) {
			RelativityHandler.removeRelative(this);
		}
	}

	/**
	 * Gets the world this tile entity is in
	 *
	 * @return The {@link World} instance
	 */
	public World getWorld() {
		return this.tile.getWorld();
	}

	/**
	 * Gets the position this tile entity in positioned in
	 *
	 * @return The {@link BlockPos} of this tile entity
	 */
	public BlockPos getPos() {
		return this.tile.getPos();
	}

	/**
	 * If the tile entity is in a loaded {@link BlockPos}
	 *
	 * @return If it is loaded
	 */
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
