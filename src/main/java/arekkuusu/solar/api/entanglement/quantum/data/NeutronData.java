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
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
@INBTData.NBTHolder(modId = "solar", name = "neutron_nbt")
public class NeutronData implements INBTData<NBTTagInt> {

	private int neutrons;

	public int get() {
		return neutrons;
	}

	public void set(int neutrons) {
		this.neutrons = neutrons;
		dirty();
	}

	@Override
	public boolean save() {
		return neutrons > 0;
	}

	@Override
	public void read(NBTTagInt tag) {
		this.neutrons = tag.getInt();
	}

	@Override
	public NBTTagInt write() {
		return new NBTTagInt(neutrons);
	}
}
