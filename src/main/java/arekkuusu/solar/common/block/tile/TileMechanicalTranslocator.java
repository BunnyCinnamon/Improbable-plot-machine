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
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by <Snack> on 17/01/2018.
 * It's distributed as part of Solar.
 */
public class TileMechanicalTranslocator extends TileRelativeBase {

	private boolean powered;

	public void translocate() {
		if(!world.isRemote && canSend()) {
			List<TileMechanicalTranslocator> list = RelativityHandler.getRelatives(this).stream()
					.filter(tile -> tile.isLoaded() && tile instanceof TileMechanicalTranslocator)
					.map(tile -> (TileMechanicalTranslocator) tile).collect(Collectors.toList());
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
		if(from.getAxis().isVertical() && to.getAxis().isVertical() && from.getOpposite() == to) {
			//TODO: Up-Down
		} else {
			if(from.getAxis().isVertical() && to.getAxis().isHorizontal()) {
				//TODO: Horizontal-Vertical
			} else if((from.getAxis().isHorizontal() && to.getAxis().isVertical())) {
				//TODO: Vertical-Horizontal
			}
			state = state.withRotation(getHorizontalRotation(from, to));
		}
		world.setBlockState(pos, state);
		getTile(TileEntity.class, world, pos).ifPresent(tile -> {
			NBTTagCompound tag = data.getRight();
			tag.setInteger("x", pos.getX());
			tag.setInteger("y", pos.getY());
			tag.setInteger("z", pos.getZ());
			tile.readFromNBT(tag);
		});
	}

	private Rotation getHorizontalRotation(EnumFacing from, EnumFacing to) {
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
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
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

	@Override
	void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		this.powered = compound.getBoolean("powered");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		compound.setBoolean("powered", powered);
	}
}
