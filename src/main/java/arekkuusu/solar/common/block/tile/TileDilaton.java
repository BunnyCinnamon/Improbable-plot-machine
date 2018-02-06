/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.block.BlockDilaton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

/**
 * Created by <Snack> on 04/02/2018.
 * It's distributed as part of Solar.
 */
public class TileDilaton extends TileBase implements ITickable {

	private BlockDilaton.DilatonHead mode = BlockDilaton.DilatonHead.DEFAULT;

	@Override
	public void update() {

	}

	public void setMode(BlockDilaton.DilatonHead mode) {
		this.mode = mode;
	}

	public BlockDilaton.DilatonHead getMode() {
		return mode;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		mode = compound.getBoolean("override") ? BlockDilaton.DilatonHead.OVERRIDE : BlockDilaton.DilatonHead.DEFAULT;
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setBoolean("override", mode == BlockDilaton.DilatonHead.OVERRIDE);
	}
}
