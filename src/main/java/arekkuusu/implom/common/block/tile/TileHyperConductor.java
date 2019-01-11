/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.common.block.BlockHyperConductor.Constants;
import arekkuusu.implom.common.block.ModBlocks;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

/**
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileHyperConductor extends TileBase {

	private HashSet<BlockPos> electrons = Sets.newHashSet();
	private boolean powered;

	@Override
	public void onLoad() {
		if(!world.isRemote) {
			BlockPos from = pos.add(
					-Constants.ELECTRIC_FIELD_REACH,
					-Constants.ELECTRIC_FIELD_REACH,
					-Constants.ELECTRIC_FIELD_REACH
			);
			BlockPos to = pos.add(
					Constants.ELECTRIC_FIELD_REACH,
					Constants.ELECTRIC_FIELD_REACH,
					Constants.ELECTRIC_FIELD_REACH
			);
			BlockPos.getAllInBox(from, to).forEach(this::addElectron);
		}
	}

	public void hyperInduceAtmosphere() {
		if(!world.isRemote) {
			electrons.removeIf(p -> world.isValid(p) && world.isBlockLoaded(p)
					&& (world.getBlockState(p).getBlock() != ModBlocks.ELECTRON || !isInRange(p)));
			electrons.stream().filter(p -> isValid(p) && isInRange(p)).forEach(this::inverseElectron);
			markDirty();
		}
	}

	public void addElectron(BlockPos pos) {
		if(!world.isRemote) {
			if(isValid(pos) && isInRange(pos) && electrons.add(pos)) {
				if(isPoweredLazy()) { //If the tile is ON, then it must inverse the electron
					inverseElectron(pos);
				}
				markDirty();
			}
		}
	}

	private boolean isValid(BlockPos pos) {
		return world.isValid(pos) && world.isBlockLoaded(pos)
				&& world.getBlockState(pos).getBlock() == ModBlocks.ELECTRON;
	}

	private boolean isInRange(BlockPos pos) {
		return getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= Constants.ELECTRIC_FIELD_REACH;
	}

	private void inverseElectron(BlockPos pos) {
		if(!world.isRemote) {
			IBlockState state = world.getBlockState(pos);
			boolean powered = state.getValue(Properties.POWER) > 0;
			int power = powered ? 0 : (int) ((getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) / 8D) * (double) getPowerLazy());
			world.setBlockState(pos, state.withProperty(Properties.POWER, power));
		}
	}

	public boolean isPoweredLazy() {
		return getPowerLazy() > 0;
	}

	public int getPowerLazy() {
		return getStateValue(Properties.POWER, pos).orElse(0);
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(Properties.POWER, getRedstonePower()));
		if(powered) {
			hyperInduceAtmosphere();
		}
	}

	public int getRedstonePower() {
		int power = 0;
		for(EnumFacing facing : EnumFacing.values()) {
			int detected = world.getRedstonePower(pos.offset(facing), facing);
			if(detected > power) power = detected;
		}
		return power;
	}

	public ImmutableList<BlockPos> getElectrons() {
		return ImmutableList.copyOf(electrons);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		NBTTagList list = (NBTTagList) compound.getTag("electrons");
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = list.getCompoundTagAt(i);
			BlockPos pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
			electrons.add(pos);
		}
		powered = compound.getBoolean("powered");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(BlockPos pos : electrons) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			list.appendTag(tag);
		}
		compound.setTag("electrons", list);
		compound.setBoolean("powered", powered);
	}
}
