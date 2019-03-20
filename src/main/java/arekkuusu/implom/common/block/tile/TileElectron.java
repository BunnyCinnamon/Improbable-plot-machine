/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;

/*
 * Created by <Arekkuusu> on 4/11/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileElectron extends TileBase {

	public int power;

	@Override
	void readNBT(NBTTagCompound compound) {
		power = compound.getInteger("power");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setInteger("power", power);
	}
}
