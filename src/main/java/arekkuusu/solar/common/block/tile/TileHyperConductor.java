/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.network.PacketHandler;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;

import static arekkuusu.solar.api.state.Power.*;

/**
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Solar.
 */
public class TileHyperConductor extends TileBase implements ITickable {

	private HashSet<BlockPos> electrons = new HashSet<>();
	private boolean needsUpdate;
	private boolean powered;
	private int tick;

	@Override
	public void onLoad() {
		/*BlockPos vec = new BlockPos(16, 16, 16);

		BlockPos from = pos.add(vec);
		BlockPos to = pos.subtract(vec);
		BlockPos.getAllInBox(from, to).forEach(pos -> {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == ModBlocks.ELECTRON_NODE) {
				double distance = getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
				if(distance <= 16D) {
					addElectron(world.getBlockState(pos), pos);
				}
			}
		});*/
	}

	@Override
	public void update() {
		if(needsUpdate) {
			PacketHandler.updatePosition(world, pos);
			needsUpdate = false;
		}
		electrons.removeIf(pos -> world.getBlockState(pos).getBlock() != ModBlocks.ELECTRON_NODE);
		if(world.isRemote && tick % 2 == 0 && !electrons.isEmpty()) {
			int index = world.rand.nextInt(electrons.size());
			int i = 0;

			for(BlockPos pos : electrons) {
				if(i == index && world.rand.nextBoolean()) {
					Vector3 from = new Vector3(getPos()).add(0.5D, 0.5D, 0.5D);
					Vector3 to = new Vector3(pos).add(0.5D, 0.5D, 0.5D);

					double distance = from.distanceTo(to) * 0.5;

					ParticleUtil.spawnBolt(world, from, to, (int) distance + 2, (float) (0.45D * distance), 0x5194FF, true);
					break;
				}
				i++;
			}
		}
		tick++;
	}

	public void hyperInduceAtmosphere() {
		if(!world.isRemote) {
			electrons.forEach(pos -> {
				IBlockState state = world.getBlockState(pos);
				electrifyElectron(state, pos);
			});
		}
	}

	private void electrifyElectron(IBlockState state, BlockPos pos) {
		int power = state.getValue(POWER_AMOUNT);
		if(power == 0) {
			power = calculatePower(pos);
		} else power = 0;

		world.setBlockState(pos, state.withProperty(POWER_AMOUNT, power));
	}

	private int calculatePower(BlockPos pos) {
		int power = 0;
		double distance = getDistanceSq(pos.getX(), pos.getY(), pos.getZ()); //Minus 1 to prevent power next to the source
		double range = 16;
		if(distance >= 1 && distance <= range) {
			power = (int) ((1 - (distance / range)) * 15D);
		}

		return power;
	}

	public void addElectron(IBlockState state, BlockPos pos) {
		if(state.getBlock() == ModBlocks.ELECTRON_NODE) {
			if(isPoweredLazy()) { //If the tile is ON, then it must inverse the electron node
				electrifyElectron(state, pos);
			} else if(state.getValue(POWER_AMOUNT) > 0) { //If the electron node is ON, then it should be powered according to its distance to the tile
				world.setBlockState(pos, state.withProperty(POWER_AMOUNT, calculatePower(pos)));
			}

			this.electrons.add(pos);
			this.markDirty();
			needsUpdate = true;
		}
	}

	public boolean isPoweredLazy() {
		return getStateValue(POWER, pos).orElse(OFF) == ON;
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(POWER, powered ? ON: OFF));
		this.hyperInduceAtmosphere();
		this.powered = powered;
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
	}
}
