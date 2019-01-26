package arekkuusu.implom.common.handler.data.capability;

import arekkuusu.implom.api.capability.ILumenCapability;
import net.minecraft.nbt.NBTTagCompound;

public class LumenCapability implements ILumenCapability {

	private int lumen;
	private int max;

	public LumenCapability(int max) {
		this.max = max;
	}

	public LumenCapability() {

	}

	@Override
	public void set(int lumen) {
		this.lumen = lumen;
	}

	@Override
	public int get() {
		return lumen;
	}

	@Override
	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public int getMax() {
		return max;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("lumen", lumen);
		nbt.setInteger("max", max);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		lumen = nbt.getInteger("lumen");
		max = nbt.getInteger("max");
	}
}
