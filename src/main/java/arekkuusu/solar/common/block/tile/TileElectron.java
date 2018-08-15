/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.state.State;
import net.minecraft.nbt.NBTTagCompound;

/*
 * Created by <Arekkuusu> on 4/11/2018.
 * It's distributed as part of Solar.
 */
public class TileElectron extends TileBase {

	public boolean isActiveLazy() {
		return getStateValue(State.POWER, pos).map(p -> p > 0).orElse(false);
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
