/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import arekkuusu.solar.api.entanglement.quantum.QuantumDataHandler;
import arekkuusu.solar.api.entanglement.quantum.data.QimranutData;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Solar.
 */
public class TileQimranut extends TileBase implements IEntangledTile {

	private UUID key = null;

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getSource().map(data -> {
			IBlockState state = world.getBlockState(data.getPos());
			if(state.getBlock().hasTileEntity(state)) {
				TileEntity tile = world.getTileEntity(data.getPos());
				return (tile != null && !(tile instanceof TileQimranut)) && tile.hasCapability(capability, data.getFacing());
			}
			return false;
		}).orElse(false) : false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getSource().map(data -> {
			IBlockState state = world.getBlockState(data.getPos());
			if(state.getBlock().hasTileEntity(state)) {
				TileEntity tile = world.getTileEntity(data.getPos());
				return tile != null && !(tile instanceof TileQimranut) ? tile.getCapability(capability, data.getFacing()) : null;
			}
			return null;
		}).orElse(null) : null;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public void setSource(BlockPos pos, EnumFacing facing) {
		getKey().ifPresent(uuid -> {
			QimranutData data = QuantumDataHandler.getOrCreate(uuid, QimranutData::new);
			data.setPos(pos);
			data.setFacing(facing);
		});
	}

	public Optional<QimranutData> getSource() {
		return getKey().map(uuid -> QuantumDataHandler.<QimranutData>get(uuid).orElse(null));
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
