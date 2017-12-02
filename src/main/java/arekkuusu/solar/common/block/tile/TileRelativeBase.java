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
public abstract class TileRelativeBase extends TileBase implements IRelativeTile {

	private UUID key;

	@Override
	public void onLoad() {
		add();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		remove();
	}

	@Override
	public void onChunkUnload() {
		if(!world.isRemote) {
			RelativityHandler.removeRelative(this, null);
		}
	}

	@Override
	public void add() {
		if(!world.isRemote) {
			RelativityHandler.addRelative(this, null);
		}
	}

	@Override
	public void remove() {
		if(!world.isRemote) {
			RelativityHandler.removeRelative(this, null);
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
		if(key == null) remove();
		this.key = key;
		updateRelativity();
	}

	abstract void updateRelativity();

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasUniqueId("key")) {
			key = compound.getUniqueId("key");
		}
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		getKey().ifPresent(key -> compound.setUniqueId("key", key));
	}
}
