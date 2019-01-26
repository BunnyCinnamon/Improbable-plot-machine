package arekkuusu.implom.api.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ILumenCapability extends INBTSerializable<NBTTagCompound> {

	void set(int lumen);

	int get();

	void setMax(int max);

	int getMax();

	default int drain(int amount, boolean drain) {
		if(amount > 0) {
			int contained = get();
			int drained = amount < getMax() ? amount : getMax();
			int remain = contained;
			int removed = remain < drained ? contained : drained;
			remain -= removed;
			if(drain) {
				set(remain);
			}
			return removed;
		} else return 0;
	}

	default int fill(int amount, boolean fill) {
		if(amount > 0) {
			int contained = get();
			int filled = contained + amount;
			int excess = filled < getMax() ? 0 : getMax() - filled;
			int added = filled - excess;
			if(fill) {
				set(added);
			}
			return excess;
		} else return amount;
	}
}
