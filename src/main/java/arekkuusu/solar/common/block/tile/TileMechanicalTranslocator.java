/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.relativity.RelativityHandler;
import arekkuusu.solar.api.helper.FacingHelper;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.util.helper.ProfilerHelper;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static net.minecraft.util.EnumFacing.*;

/*
 * Created by <Arekkuusu> on 17/01/2018.
 * It's distributed as part of Solar.
 */
public class TileMechanicalTranslocator extends TileRelativityBase implements Comparable<TileMechanicalTranslocator> {

	private boolean powered;
	private int index = -1;

	@SideOnly(Side.CLIENT)
	public float temp = -1;

	public void activate() {
		if(!world.isRemote && canSend()) {
			ProfilerHelper.begin("[Mechanical Translocator] Relocating block");
			List<TileMechanicalTranslocator> list = RelativityHandler.getRelatives(this).stream()
					.filter(tile -> tile.isLoaded() && tile instanceof TileMechanicalTranslocator)
					.map(tile -> (TileMechanicalTranslocator) tile).sorted().collect(Collectors.toList());
			int index = list.indexOf(this);
			int size = list.size();
			int i = index + 1 >= size ? 0 : index + 1;
			Triple<IBlockState, EnumFacing, NBTTagCompound> triplet = getRelativeState();
			for(; i < size; i++) {
				TileMechanicalTranslocator mechanical = list.get(i);
				if(mechanical.isTransferable() && mechanical != this && mechanical.canReceive(triplet.getLeft())) {
					clearRelativeState(); //Before sending the actual state
					mechanical.setRelativeState(triplet);
					break;
				}
				if(i == index) {
					break;
				}
				if(i + 1 >= size) {
					i = -1;
				}
			}
			ProfilerHelper.end();
		}
	}

	private void setRelativeState(Triple<IBlockState, EnumFacing, NBTTagCompound> data) {
		BlockPos pos = getPos().offset(getFacingLazy());
		IBlockState state = data.getLeft();
		EnumFacing from = data.getMiddle();
		EnumFacing to = getFacingLazy();
		world.setBlockState(pos, state.getMaterial() != Material.WATER ? getRotationState(state, from, to) : state);
		getTile(TileEntity.class, world, pos).ifPresent(tile -> {
			NBTTagCompound tag = data.getRight();
			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			tile.readFromNBT(tag);
		});
		world.notifyNeighborsOfStateChange(getPos(), getBlockType(), true);
	}

	private IBlockState getRotationState(IBlockState original, EnumFacing from, EnumFacing to) {
		ProfilerHelper.begin("[Mechanical Translocator] Rotating block");
		if(from.getAxis().isVertical() || to.getAxis().isVertical()) {
			for(IProperty<?> p : original.getPropertyKeys()) {
				if(p.getValueClass().equals(EnumFacing.class) && p.getName().toLowerCase(Locale.ROOT).contains("facing")) {
					//noinspection unchecked
					IProperty<EnumFacing> property = (IProperty<EnumFacing>) p;
					EnumFacing actual = original.getValue(property);
					if(from.getOpposite() == to) {
						actual = actual.getOpposite();
					} else {
						if(actual == from || actual == from.getOpposite()) {
							if(from.getAxis().isVertical()) {
								EnumFacing facing = to == EAST || to == WEST ? to.getOpposite() : to;
								actual = FacingHelper.rotateXY(actual, from.getAxisDirection(), facing);
							} else {
								EnumFacing facing = from == EAST || from == WEST ? from.getOpposite() : from;
								actual = FacingHelper.rotateXY(actual, to.getOpposite().getAxisDirection(), facing);
							}
						} else actual = actual.getOpposite();
					}
					original = apply(property, original, actual);
					break;
				}
			}
		}
		ProfilerHelper.end();
		return original.withRotation(FacingHelper.getHorizontalRotation(from, to));
	}

	private static IBlockState apply(IProperty<EnumFacing> property, IBlockState state, EnumFacing facing) {
		return property.getAllowedValues().contains(facing) ? state.withProperty(property, facing) : state;
	}

	private Triple<IBlockState, EnumFacing, NBTTagCompound> getRelativeState() {
		NBTTagCompound tag = new NBTTagCompound();
		BlockPos pos = getPos().offset(getFacingLazy());
		IBlockState state = world.getBlockState(pos);
		getTile(TileEntity.class, world, pos).ifPresent(tile -> {
			tile.writeToNBT(tag);
			tag.removeTag("x");
			tag.removeTag("y");
			tag.removeTag("z");
		});
		return Triple.of(state, getFacingLazy(), tag);
	}

	private void clearRelativeState() {
		BlockPos pos = getPos().offset(getFacingLazy());
		getTile(TileEntity.class, world, pos).ifPresent(tile -> {
			world.removeTileEntity(pos);
		});
		world.setBlockToAir(pos);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(UP);
	}

	public boolean isTransferable() {
		return getStateValue(State.ACTIVE, getPos()).orElse(false);
	}

	public boolean canReceive(IBlockState state) {
		BlockPos pos = getPos().offset(getFacingLazy());
		return (world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos))
				&& state.getBlock().canPlaceBlockAt(world, pos);
	}

	public boolean canSend() {
		BlockPos pos = getPos().offset(getFacingLazy());
		return !world.isAirBlock(pos) && !getTile(TileMechanicalTranslocator.class, world, pos).isPresent();
	}

	public void setTransferable(boolean transferable) {
		if(isTransferable() != transferable) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(State.ACTIVE, transferable));
		}
	}

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		this.powered = powered;
		this.markDirty();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void add() {
		if(!world.isRemote) {
			RelativityHandler.addRelative(this, null);
			if(index == -1) {
				this.index = RelativityHandler.getRelatives(this).indexOf(this);
				this.markDirty();
			}
		}
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		this.powered = compound.getBoolean("powered");
		this.index = compound.getInteger("index");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		compound.setBoolean("powered", powered);
		compound.setInteger("index", index);
	}

	@Override
	public int compareTo(TileMechanicalTranslocator other) {
		return Integer.compare(index, other.index);
	}
}
