package arekkuusu.implom.api.capability.nbt;

import arekkuusu.implom.api.capability.ILumenCapability;
import arekkuusu.implom.api.capability.data.IntNBTData;

public interface ILumenNBTDataCapability extends ILumenCapability, INBTDataCapability<IntNBTData> {

	@Override
	default void set(int lumen) {
		getData(getKey()).ifPresent(data -> data.setValue(lumen));
	}

	@Override
	default int get() {
		return getData(getKey()).map(IntNBTData::getValue).orElse(0);
	}

	@Override
	default void setMax(int max) {
		getData(getKey()).ifPresent(data -> data.setMax(max));
	}

	@Override
	default int getMax() {
		return getData(getKey()).map(IntNBTData::getMax).orElse(0);
	}

	@Override
	default Class<IntNBTData> getDataClass() {
		return IntNBTData.class;
	}
}
