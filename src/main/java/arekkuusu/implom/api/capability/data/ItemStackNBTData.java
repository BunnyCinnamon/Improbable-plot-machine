package arekkuusu.implom.api.capability.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@INBTData.NBTHolder(modId = "improbableplotmachine", name = "itemstack_nbt")
public class ItemStackNBTData implements INBTData<NBTTagCompound> {

	private ItemStack stack = ItemStack.EMPTY;

	public void setStack(ItemStack stack) {
		this.stack = stack;
		this.markDirty();
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public boolean canDeserialize() {
		return stack != ItemStack.EMPTY;
	}

	@Override
	public void deserialize(NBTTagCompound nbt) {
		stack = new ItemStack(nbt);
	}

	@Override
	public NBTTagCompound serialize() {
		return stack.serializeNBT();
	}
}
