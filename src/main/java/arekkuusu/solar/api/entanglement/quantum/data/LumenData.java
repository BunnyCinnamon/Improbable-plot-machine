/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.quantum.data;

import net.minecraft.nbt.NBTTagInt;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
@INBTData.NBTHolder(modId = "solar", name = "lumen_nbt")
public class LumenData implements INBTData<NBTTagInt> {

	private int lumen;

	public int get() {
		return lumen;
	}

	public void set(int neutrons) {
		this.lumen = neutrons;
		dirty();
	}

	@Override
	public boolean save() {
		return lumen > 0;
	}

	@Override
	public void read(NBTTagInt tag) {
		this.lumen = tag.getInt();
	}

	@Override
	public NBTTagInt write() {
		return new NBTTagInt(lumen);
	}
}
