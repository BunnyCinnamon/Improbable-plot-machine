/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

/**
 * Created by <Arekkuusu> on 18/07/2017.
 * It's distributed as part of Solar.
 */
public abstract class TileBase extends TileEntity {

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	<T extends Comparable<T>> Optional<T> getStateValue(IProperty<T> property, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getPropertyKeys().contains(property) ? Optional.of(state.getValue(property)) : Optional.empty();
	}

	@SuppressWarnings("unchecked")
	<T extends TileEntity> Optional<T> getTile(Class<T> clazz, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		return clazz.isInstance(tile) ? Optional.of((T) tile) : Optional.empty();
	}

	@Override
	public double getDistanceSq(double x, double y, double z) {
		return Math.sqrt(super.getDistanceSq(x, y, z));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		readNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		writeNBT(tag);
		return tag;
	}

	abstract void readNBT(NBTTagCompound compound);

	abstract void writeNBT(NBTTagCompound compound);
}
