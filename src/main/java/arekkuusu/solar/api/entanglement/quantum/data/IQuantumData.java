/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum.data;

import arekkuusu.solar.api.SolarApi;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by <Snack> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public interface IQuantumData<T extends NBTBase> extends INBTSerializable<NBTTagCompound> {

	@Override
	default void deserializeNBT(NBTTagCompound nbt) {
		//noinspection unchecked
		read((T) nbt.getTag("data"));
	}

	@Override
	default NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("data", write());
		return tag;
	}

	default void dirty() {
		SolarApi.getQuantumData().markDirty();
	}

	boolean save();
	void read(T tag);
	T write();
}
