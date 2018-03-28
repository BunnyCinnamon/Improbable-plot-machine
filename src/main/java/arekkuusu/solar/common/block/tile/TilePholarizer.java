package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.block.BlockPholarizer;
import arekkuusu.solar.common.block.BlockPholarizer.Polarization;
import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TilePholarizer extends TileBase {

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public Polarization getPolarizationLazy() {
		return getStateValue(BlockPholarizer.POLARIZATION, pos).orElse(Polarization.POSITIVE);
	}

	@Override
	void readNBT(NBTTagCompound compound) {

	}

	@Override
	void writeNBT(NBTTagCompound compound) {

	}
}
