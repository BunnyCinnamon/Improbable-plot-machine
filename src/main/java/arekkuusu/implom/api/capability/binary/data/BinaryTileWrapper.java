/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.binary.data;

import arekkuusu.implom.api.capability.binary.BinaryHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class BinaryTileWrapper<T extends TileEntity> implements IBinary {

	private final T tile;
	private UUID key;

	public BinaryTileWrapper(T tile) {
		this.tile = tile;
	}

	@Override
	public void add() {
		if(!getWorld().isRemote) {
			BinaryHandler.add(this);
		}
	}

	@Override
	public void remove() {
		if(!getWorld().isRemote) {
			BinaryHandler.remove(this);
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
