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

/**
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Solar.
 */
public class TileHyperConductor extends TileBase implements ITickable {

	private HashSet<BlockPos> electrons = Sets.newHashSet();
	private boolean needsUpdate;
	private boolean powered;
	private int tick;

	@Override
	public void onLoad() {
		BlockPos vec = new BlockPos(8, 8, 8);
		BlockPos from = pos.add(vec);
		BlockPos to = pos.subtract(vec);
		BlockPos.getAllInBox(from, to).forEach(pos -> {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == ModBlocks.ELECTRON) {
				double distance = getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
				if(distance <= 8D) {
					addElectron(world.getBlockState(pos), pos);
				}
			}
		});
	}

	@Override
	public void update() {
		if(needsUpdate) {
			updatePosition(world, pos);
			needsUpdate = false;
		}
		electrons.removeIf(pos -> world.getBlockState(pos).getBlock() != ModBlocks.ELECTRON);
		if(world.isRemote && isPoweredLazy() && tick++ % 2 == 0) {
			if(!electrons.isEmpty() && world.rand.nextInt(6) == 0) {
				Vector3 from = new Vector3(getPos()).grow(0.5D);
				int index = world.rand.nextInt(electrons.size());
				int i = 0;
				for(BlockPos pos : electrons) {
					if(i++ == index && world.isBlockLoaded(pos)) {
						Vector3 to = new Vector3(pos).add(Vector3.getRandomVec(0.1F)).grow(0.5D);
						to.subtract(to.copy().subtract(from).multiply(0.1D));
						double distance = Math.min(4D, from.distanceTo(to)) * 0.5;
						ParticleUtil.spawnBolt(world, from, to, (int) distance + 3, (float) (0.45D * distance), 0x5194FF, true);
						((WorldClient) world).playSound(getPos(), SolarSounds.SPARK, SoundCategory.NEUTRAL, 0.1F, 1F, false);
						break;
					}
				}
			}
		}
	}

	public void hyperInduceAtmosphere() {
		if(!world.isRemote) {
			electrons.stream()
					.filter(pos -> world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock() == ModBlocks.ELECTRON)
					.forEach(pos -> {
						IBlockState state = world.getBlockState(pos);
						electrifyElectron(state, pos);
					});
		}
	}

	private void electrifyElectron(IBlockState state, BlockPos pos) {
		int power = state.getValue(State.POWER);
		if(power == 0) {
			power = calculatePower(pos);
		} else power = 0;

		world.setBlockState(pos, state.withProperty(State.POWER, power));
	}

	private int calculatePower(BlockPos pos) {
		int power = 0;
		double distance = getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
		double range = Math.min(getPowerLazy(), 8D);
		if(distance >= 1 && distance <= range) {
			power = (int) ((1 - (distance / range)) * 15D);
		}

		return power;
	}

	public void addElectron(IBlockState state, BlockPos pos) {
		if(state.getBlock() == ModBlocks.ELECTRON && electrons.add(pos)) {
			if(isPoweredLazy()) { //If the tile is ON, then it must inverse the electron node
				electrifyElectron(state, pos);
			} else if(state.getValue(State.POWER) > 0) { //If the electron node is ON, then it should be powered according to its distance to the tile
				world.setBlockState(pos, state.withProperty(State.POWER, calculatePower(pos)));
			}

			this.markDirty();
			needsUpdate = true;
		}
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
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(State.POWER, getRedstonePower()));
		this.hyperInduceAtmosphere();
		this.powered = powered;
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
