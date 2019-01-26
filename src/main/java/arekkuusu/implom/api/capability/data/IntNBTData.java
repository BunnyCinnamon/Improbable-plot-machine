package arekkuusu.implom.api.capability.data;

import net.minecraft.nbt.NBTTagCompound;

@INBTData.NBTHolder(modId = "improbableplotmachine", name = "int_nbt")
public class IntNBTData implements INBTData<NBTTagCompound> {

	private int value;
	private int max;
	private int min;

	public void setValue(int value) {
		if(value > max) value = max;
		else if(value < min) value = min;
		this.value = value;
		this.markDirty();
	}

	public int getValue() {
		return value;
	}

	public void setMax(int max) {
		this.max = max;
		if(value > max) value = max;
		this.markDirty();
	}

	public int getMax() {
		return max;
	}

	public void setMin(int min) {
		this.min = min;
		if(value < min) value = min;
		this.markDirty();
	}

	public int getMin() {
		return min;
	}

	@Override
	public boolean canDeserialize() {
		return getValue() != 0;
	}

	@Override
	public void deserialize(NBTTagCompound nbt) {
		value = nbt.getInteger("value");
		min = nbt.getInteger("min");
		max = nbt.getInteger("max");
	}

	@Override
	public NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("value", value);
		nbt.setInteger("min", min);
		nbt.setInteger("max", max);
		return nbt;
	}
}
