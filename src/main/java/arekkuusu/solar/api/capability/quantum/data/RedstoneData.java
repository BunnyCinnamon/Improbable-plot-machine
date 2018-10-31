/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.quantum.data;

import net.minecraft.nbt.NBTTagInt;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Solar.
 */
@INBTData.NBTHolder(modId = "solar", name = "redstone_nbt")
public class RedstoneData implements INBTData<NBTTagInt> {

	private int i;

	public void set(int i) {
		if(i > 15) i = 15;
		if(i < 0) i = 0;
		this.i = i;
		dirty();
	}

	public int get() {
		return i;
	}

	@Override
	public boolean canDeserialize() {
		return i > 0;
	}

	@Override
	public NBTTagInt serialize() {
		return new NBTTagInt(i);
	}

	@Override
	public void deserialize(NBTTagInt tag) {
		this.i = tag.getInt();
	}
}
