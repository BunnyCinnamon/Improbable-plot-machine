/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.relativity.RelativityHandler;
import arekkuusu.solar.api.state.State;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.EnumFacing.*;

/**
 * Created by <Snack> on 17/01/2018.
 * It's distributed as part of Solar.
 */
public class TileMechanicalTranslocator extends TileRelativeBase implements Comparable<TileMechanicalTranslocator> {

	private boolean powered;
	private int index = -1;

	public void translocate() {
		if(!world.isRemote && canSend()) {
			List<TileMechanicalTranslocator> list = RelativityHandler.getRelatives(this).stream()
					.filter(tile -> tile.isLoaded() && tile instanceof TileMechanicalTranslocator)
					.map(tile -> (TileMechanicalTranslocator) tile).collect(Collectors.toList());
			Collections.sort(list);
			int index = list.indexOf(this);
			int size = list.size();
			int i = index + 1 >= size ? 0 : index + 1;
			for(; i < size; i++) {
				TileMechanicalTranslocator mechanical = list.get(i);
				if(mechanical.isTransferable() && mechanical != this && mechanical.canReceive()) {
					mechanical.setRelativeState(getRelativeState());
					break;
				}
				if(i + 1 >= size) {
					i = -1;
				}
				if(i == index) {
					break;
				}
			}
		}
	}

	private void setRelativeState(Triple<IBlockState, EnumFacing, NBTTagCompound> data) {
		BlockPos pos = getPos().offset(getFacingLazy());
		IBlockState state = data.getLeft();
		EnumFacing from = data.getMiddle();
		EnumFacing to = getFacingLazy();
		if(state.getBlock().canPlaceBlockAt(world, pos)) {
			world.setBlockState(pos, getRotationState(state, from, to).withRotation(getHorizontalRotation(from, to)));
			getTile(TileEntity.class, world, pos).ifPresent(tile -> {
				NBTTagCompound tag = data.getRight();
				tag.setInteger("x", pos.getX());
				tag.setInteger("y", pos.getY());
				tag.setInteger("z", pos.getZ());
				tile.readFromNBT(tag);
			});
		}
	}

	@SuppressWarnings("unchecked")
	private IBlockState getRotationState(IBlockState original, EnumFacing from, EnumFacing to) {
		if(from.getAxis().isVertical() || to.getAxis().isVertical()) {
			for(IProperty<?> p : original.getPropertyKeys()) {
				if(p.getValueClass().equals(EnumFacing.class)) {
					IProperty<EnumFacing> property = (IProperty<EnumFacing>) p;
					EnumFacing actual = original.getValue(property);
					if(from.getOpposite() == to) {
						actual = actual.getOpposite();
					} else {
						if(actual == from || actual == from.getOpposite()) {
							if(from.getAxis().isVertical()) {
								to = to == EAST || to == WEST ? to.getOpposite() : to;
								actual = rotateXY(actual, from.getAxisDirection(), to);
							} else {
								from = from == EAST || from == WEST ? from.getOpposite() : from;
								actual = rotateXY(actual, to.getOpposite().getAxisDirection(), from);
							}
						} else actual = actual.getOpposite();
					}
					original = apply(property, original, actual);
					break;
				}
			}
		}
		return original;
	}

	private static IBlockState apply(IProperty<EnumFacing> property, IBlockState state, EnumFacing facing) {
		return property.getAllowedValues().contains(facing) ? state.withProperty(property, facing) : state;
	}

	private static EnumFacing rotateXY(EnumFacing actual, AxisDirection direction, EnumFacing b) {
		switch(direction) {
			case POSITIVE:
				switch(b) {
					case NORTH:
					case SOUTH:
						actual = rotateX(actual, b == SOUTH);
						break;
					case WEST:
					case EAST:
						actual = rotateZ(actual, b == EAST);
						break;
				}
				break;
			case NEGATIVE:
				switch(b) {
					case NORTH:
					case SOUTH:
						actual = rotateX(actual, b == NORTH);
						break;
					case WEST:
					case EAST:
						actual = rotateZ(actual, b == WEST);
						break;
				}
				break;
		}
		return actual;
	}

	private static EnumFacing rotateX(EnumFacing facing, boolean inverse) {
		if(!inverse) {
			return facing.rotateAround(Axis.X);
		} else {
			switch(facing) {
				case NORTH:
					return UP;
				case SOUTH:
					return DOWN;
				case UP:
					return SOUTH;
				case DOWN:
					return NORTH;
				case EAST:
				case WEST:
				default:
					throw new IllegalStateException("Unable to get X-rotated facing of " + facing);
			}
		}
	}

	private static EnumFacing rotateZ(EnumFacing facing, boolean inverse) {
		if(!inverse) {
			return facing.rotateAround(Axis.Z);
		} else {
			switch(facing) {
				case EAST:
					return UP;
				case WEST:
					return DOWN;
				case UP:
					return WEST;
				case DOWN:
					return EAST;
				case SOUTH:
				default:
					throw new IllegalStateException("Unable to get Z-rotated facing of " + facing);
			}
		}
	}

	private static Rotation getHorizontalRotation(EnumFacing from, EnumFacing to) {
		if(from.getAxis().isVertical() || to.getAxis().isVertical()) return Rotation.NONE;
		if(from.getOpposite() == to) {
			return Rotation.CLOCKWISE_180;
		} else if(from != to) {
			int indexFrom = from.getHorizontalIndex();
			int indexTo = to.getHorizontalIndex();
			if(indexFrom < indexTo || (indexFrom == 3 && indexTo == 0)) {
				return Rotation.CLOCKWISE_90;
			} else {
				return Rotation.COUNTERCLOCKWISE_90;
			}
		}
		return Rotation.NONE;
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
			world.removeTileEntity(pos);
		});
		world.setBlockToAir(pos);
		return Triple.of(state, getFacingLazy(), tag);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(UP);
	}

	public boolean isTransferable() {
		return getStateValue(State.ACTIVE, getPos()).orElse(false);
	}

	public boolean canReceive() {
		BlockPos pos = getPos().offset(getFacingLazy());
		return world.isAirBlock(pos) || world.getBlockState(pos).getBlock().isReplaceable(world, pos);
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
		return Integer.compare(other.index, index);
	}
}
