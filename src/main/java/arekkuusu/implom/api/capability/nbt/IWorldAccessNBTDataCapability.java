package arekkuusu.implom.api.capability.nbt;

import arekkuusu.implom.api.capability.data.WorldAccessNBTData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface IWorldAccessNBTDataCapability extends INBTDataCapability<WorldAccessNBTData>, INBTSerializable<NBTTagCompound> {

	@Nullable
	default WorldAccessNBTData get() {
		return getData(getKey()).orElse(null);
	}

	@Override
	default Class<WorldAccessNBTData> getDataClass() {
		return WorldAccessNBTData.class;
	}
}
