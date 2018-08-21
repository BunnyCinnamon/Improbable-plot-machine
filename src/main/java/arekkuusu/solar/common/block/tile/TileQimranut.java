/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.quantum.IQuantum;
import arekkuusu.solar.api.capability.quantum.QuantumDataHandler;
import arekkuusu.solar.api.capability.quantum.data.QimranutData;
import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Solar.
 */
public class TileQimranut extends TileBase implements IQuantum {

	private UUID key = null;

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getSource().map(data -> getTargetTile()
				.filter(tile -> !(tile instanceof TileQimranut))
				.map(tile -> tile.hasCapability(capability, data.getFacing()))
				.orElse(null)
		).orElse(false) : false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getSource().map(data -> getTargetTile()
				.filter(tile -> !(tile instanceof TileQimranut))
				.map(tile -> tile.getCapability(capability, data.getFacing()))
				.orElse(null)
		).orElse(null) : null;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public void setSource(World world, BlockPos pos, EnumFacing facing) {
		getKey().ifPresent(uuid -> {
			QimranutData data = QuantumDataHandler.getOrCreate(uuid, QimranutData::new);
			data.setPos(pos);
			data.setFacing(facing);
			data.setWorld(world);
		});
	}

	public Optional<QimranutData> getSource() {
		return getKey().map(uuid -> QuantumDataHandler.<QimranutData>get(uuid).orElse(null));
	}

	public Optional<TileEntity> getTargetTile() {
		return getSource().filter(source -> source.getWorld() != null)
				.flatMap(source -> getTile(TileEntity.class, source.getWorld(), source.getPos()));
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
	}

	@Override
	public void readNBT(NBTTagCompound compound) {
		if(compound.hasUniqueId("key")) {
			setKey(compound.getUniqueId("key"));
		}
	}

	@Override
	public void writeNBT(NBTTagCompound compound) {
		getKey().ifPresent(key -> compound.setUniqueId("key", key));
	}
}
