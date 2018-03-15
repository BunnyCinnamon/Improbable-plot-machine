/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum.data;

import net.minecraft.nbt.NBTTagInt;

/**
 * Created by <Snack> on 14/03/2018.
 * It's distributed as part of Solar.
 */
@INBTData.NBTHolder(modId = "solar", name = "power_data")
public class PowerData implements INBTData<NBTTagInt> {

	private int i;

	public void setI(int i) {
		if(i > 15) i = 15;
		if(i < 0) i = 0;
		this.i = i;
		dirty();
	}

	public int getI() {
		return i;
	}

	@Override
	public boolean save() {
		return i != 0;
	}

	@Override
	public NBTTagInt write() {
		return new NBTTagInt(i);
	}

	@Override
	public void read(NBTTagInt tag) {
		this.i = tag.getInt();
	}
}
