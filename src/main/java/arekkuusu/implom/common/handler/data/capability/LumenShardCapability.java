package arekkuusu.implom.common.handler.data.capability;

import arekkuusu.implom.api.capability.ILumenCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LumenShardCapability implements ILumenCapability {

	public ItemStack stack;

	public LumenShardCapability(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public int drain(int amount, boolean drain) {
		if(amount > 0) {
			int contained = get();
			int drained = amount < getMax() ? amount : getMax();
			int remain = contained;
			int removed = drained % 10 == 0 ? remain < drained ? contained : drained : 0;
			remain -= removed;
			if(drain) {
				set(remain);
			}
			return removed;
		} else return 0;
	}

	@Override
	public int fill(int amount, boolean fill) {
		if(amount > 0) {
			int contained = get();
			int filled = contained + amount;
			int excess = filled < getMax() ? 0 : getMax() - filled;
			int added = filled - excess;
			if(added % 10 != 0) {
				excess = amount;
				added = contained;
			}
			if(fill) {
				set(added);
			}
			return excess;
		} else return amount;
	}

	@Override
	public void set(int lumen) {
		stack.setCount(lumen / 10);
	}

	@Override
	public int get() {
		return stack.getCount() * 10;
	}

	@Override
	public void setMax(int max) {
		//Nop
	}

	@Override
	public int getMax() {
		return stack.getMaxStackSize() * 10;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return new NBTTagCompound(); //No no no
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		//No no
	}
}
