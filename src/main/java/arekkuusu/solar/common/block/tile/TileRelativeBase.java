/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 28/09/2017.
 * It's distributed as part of Solar.
 */
public abstract class TileRelativeBase<T extends TileRelativeBase> extends TileBase implements IRelativeTile<T> {

	private UUID key;

	@Override
	public void onLoad() {
		if(!world.isRemote) add();
	}

	@Override
	public void validate() {
		super.validate();
		if(!world.isRemote) add();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if(!world.isRemote) remove();
	}

	@Override
	public void onChunkUnload() {
		RelativityHandler.removeRelative(this, tile -> onUnload());
	}

	abstract void onUnload();

	@Override
	public void add() {
		RelativityHandler.addRelative(this, tile -> onAdd());
	}

	abstract void onAdd();

	@Override
	public void remove() {
		RelativityHandler.removeRelative(this, tile -> onRemove());
	}

	abstract void onRemove();

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
		if(key == null) remove();
		this.key = key;
		updateRelativity();
	}

	abstract void updateRelativity();

	@Override
	void readNBT(NBTTagCompound cmp) {
		if(cmp.hasUniqueId("uuid_key")) {
			key = cmp.getUniqueId("uuid_key");
		}
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		if(key != null) {
			cmp.setUniqueId("uuid_key", key);
		}
	}
}
