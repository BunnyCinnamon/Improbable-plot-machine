/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.api.sound.SolarSounds;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.network.PacketHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Solar.
 */
public class TileHyperConductor extends TileBase implements ITickable {

	private HashSet<BlockPos> electrons = Sets.newHashSet();
	private boolean needsUpdate;
	private boolean powered;

	@Override
	public void update() {
		if(!world.isRemote && needsUpdate) {
			updatePosition(world, pos);
			needsUpdate = false;
		}
		electrons.removeIf(pos -> world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock() != ModBlocks.ELECTRON);
	}

	public void hyperInduceAtmosphere() {
		if(!world.isRemote) {
			electrons.forEach(this::inverseElectron);
		}
	}

	private void inverseElectron(BlockPos pos) {
		if(!world.isRemote) {
			IBlockState state = world.getBlockState(pos);
			boolean powered = state.getValue(State.POWER) > 0;
			world.setBlockState(pos, state.withProperty(State.POWER, powered ? 0 : getPowerLazy()));
		}
	}

	public void addElectron(BlockPos pos) {
		if(world.isRemote || !isInRange(pos)) return;
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == ModBlocks.ELECTRON && electrons.add(pos)) {
			if(isPoweredLazy()) { //If the tile is ON, then it must inverse the electron node
				inverseElectron(pos);
			}
			this.markDirty();
			needsUpdate = true;
		}
	}

	public boolean isInRange(BlockPos pos) {
		return getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 8;
	}

	public int getPowerLazy() {
		return getStateValue(State.POWER, pos).orElse(0);
	}

	public boolean isPoweredLazy() {
		return getPowerLazy() > 0;
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(State.POWER, getRedstonePower()));
		this.hyperInduceAtmosphere();
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
		for(BlockPos pos: electrons) {
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
