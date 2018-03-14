/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

/**
 * Created by <Snack> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public class QimranutData implements IQuantumData<NBTTagCompound> {

	private BlockPos pos;
	private EnumFacing facing;

	public void setPos(BlockPos pos) {
		this.pos = pos;
		dirty();
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setFacing(@Nullable EnumFacing facing) {
		this.facing = facing;
		dirty();
	}

	@Nullable
	public EnumFacing getFacing() {
		return facing;
	}

	@Override
	public boolean save() {
		return pos != null;
	}

	@Override
	public void read(NBTTagCompound tag) {
		this.pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
		if(tag.hasKey("facing")) {
			this.facing = EnumFacing.byName(tag.getString("facing"));
		} else this.facing = null;
	}

	@Override
	public NBTTagCompound write() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", pos.getX());
		tag.setInteger("y", pos.getY());
		tag.setInteger("z", pos.getZ());
		if(facing != null) {
			tag.setString("facing", facing.getName());
		}
		return tag;
	}
}
