/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.common.block.BlockHyperConductor.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileHyperConductor extends TileBase {

	public boolean powered;

	private boolean isPositionInRange(BlockPos pos) {
		return getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= Constants.ELECTRIC_FIELD_REACH;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		powered = compound.getBoolean("powered");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setBoolean("powered", powered);
	}
}
