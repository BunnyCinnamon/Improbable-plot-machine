package arekkuusu.implom.api.capability.nbt;

import arekkuusu.implom.api.capability.data.IntNBTData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IRedstoneNBTCapability extends INBTDataCapability<IntNBTData>, INBTSerializable<NBTTagCompound> {

	default void set(int level) {
		getData(getKey()).ifPresent(data -> data.setValue(level));
	}

	default int get() {
		return getData(getKey()).map(IntNBTData::getValue).orElse(0);
	}

	@Override
	default Class<IntNBTData> getDataClass() {
		return IntNBTData.class;
	}
}
