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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by <Snack> on 14/03/2018.
 * It's distributed as part of Solar.
 */
public interface INBTData<T extends NBTBase> extends INBTSerializable<NBTTagCompound> {

	String NBT_KEY = "data";

	/**
	 * Checks whether the {@link INBTData} should persists between saves.
	 * It is used to remove data which is unused or no longer needed
	 *
	 * @return If it should save
	 */
	boolean save();

	/**
	 * Reads the data of the {@param tag} NBT.
	 * It is called once, when first loading the data
	 *
	 * @param tag The {@link T} NBT
	 */
	void read(T tag);

	/**
	 * Writes itself to a {@link T} NBT
	 *
	 * @return The {@link T} NBT
	 */
	T write();

	/**
	 * Marks dirty World Data to save changes
	 */
	default void dirty() {
		SolarApi.getWorldData().markDirty();
	}

	@Override
	default void deserializeNBT(NBTTagCompound nbt) {
		//noinspection unchecked
		read((T) nbt.getTag(NBT_KEY));
	}

	@Override
	default NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(NBT_KEY, write());
		return tag;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface NBTHolder {
		/**
		 * The unique mod Identifier of this mod.
		 */
		String modId();

		/**
		 * The unique Identifier of the saved data.
		 * It mustn't change so the data persists even if you rename the class
		 */
		String name();
	}
}
