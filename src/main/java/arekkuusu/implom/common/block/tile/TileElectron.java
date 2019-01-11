/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.state.Properties;
import net.minecraft.nbt.NBTTagCompound;

/*
 * Created by <Arekkuusu> on 4/11/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileElectron extends TileBase {

	public boolean isActiveLazy() {
		return getStateValue(Properties.POWER, pos).map(p -> p > 0).orElse(false);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		//NO-OP
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		//NO-OP
	}
}
