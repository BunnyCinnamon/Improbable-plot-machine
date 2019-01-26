package arekkuusu.implom.api.capability.data;

import arekkuusu.implom.api.IPMApi;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface INBTData<T extends NBTBase> extends INBTSerializable<NBTBase> {

	void deserialize(T nbt);

	T serialize();

	default boolean canDeserialize() {
		return true;
	}

	default void markDirty() {
		IPMApi.getInstance().markWorldDirty();
	}

	@Override
	default NBTBase serializeNBT() {
		return serialize();
	}

	@Override
	@SuppressWarnings("unchecked")
	default void deserializeNBT(NBTBase nbt) {
		deserialize((T) nbt);
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
