/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.binary.BinaryHandler;
import arekkuusu.solar.api.capability.binary.ISimpleBinaryTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Solar.
 */
public abstract class TileBinaryBase extends TileBase implements ISimpleBinaryTile {

	private UUID key;

	@Override
	public Optional<ISimpleBinaryTile> getInverse() {
		return isLoaded() ? Optional.ofNullable(BinaryHandler.getInverse(this)) : Optional.empty();
	}

	@Override
	public void onLoad() {
		if(!world.isRemote) {
			add();
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		remove();
	}

	@Override
	public void onChunkUnload() {
		if(!world.isRemote) {
			BinaryHandler.remove(this);
		}
	}

	@Override
	public void add() {
		if(!world.isRemote) {
			BinaryHandler.add(this);
		}
	}

	@Override
	public void remove() {
		if(!world.isRemote) {
			BinaryHandler.remove(this);
		}
	}

	@Override
	public World getRelativeWorld() {
		return world;
	}

	@Override
	public BlockPos getRelativePos() {
		return pos;
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		remove();
		this.key = key;
		add();
		markDirty();
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasUniqueId("key")) {
			UUID key = compound.getUniqueId("key");
			if(world != null) {
				remove();
				this.key = key;
				add();
			} else this.key = key;
		}
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		getKey().ifPresent(key -> compound.setUniqueId("key", key));
	}
}
