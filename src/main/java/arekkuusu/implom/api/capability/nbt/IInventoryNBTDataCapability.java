package arekkuusu.implom.api.capability.nbt;

import arekkuusu.implom.api.capability.data.ItemStackNBTData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public interface IInventoryNBTDataCapability extends IItemHandlerModifiable, INBTDataCapability<ItemStackNBTData>, INBTSerializable<NBTTagCompound> {

	@Nonnull
	@Override
	default ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if(stack.isEmpty()) return ItemStack.EMPTY;
		if(getKey() == null || !isItemValid(slot, stack)) return stack;
		ItemStack existing = getStackInSlot(slot);
		int limit = stack.getMaxStackSize();
		if(!existing.isEmpty()) {
			if(!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
				return stack;
			}
			limit -= existing.getCount();
		}
		if(limit <= 0) return stack;
		boolean reachedLimit = stack.getCount() > limit;
		if(!simulate) {
			if(existing.isEmpty()) {
				setStackInSlot(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
				onChange(existing);
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
				setStackInSlot(slot, existing);
				onChange(existing);
			}
		}
		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	default ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(amount == 0 || getKey() == null) return ItemStack.EMPTY;
		ItemStack existing = getStackInSlot(slot);
		if(existing.isEmpty()) {
			return ItemStack.EMPTY;
		}
		int toExtract = Math.min(amount, existing.getMaxStackSize());
		if(existing.getCount() <= toExtract) {
			if(!simulate) {
				setStackInSlot(slot, ItemStack.EMPTY);
				onChange(existing);
			}
			return existing;
		} else {
			if(!simulate) {
				setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				onChange(existing);
			}
			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	default void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		getData(getKey()).ifPresent(data -> data.setStack(stack));
	}

	@Nonnull
	@Override
	default ItemStack getStackInSlot(int slot) {
		return getData(getKey()).map(ItemStackNBTData::getStack).orElse(ItemStack.EMPTY);
	}

	@Override
	default int getSlotLimit(int slot) {
		return getData(getKey()).map(ItemStackNBTData::getStack).map(ItemStack::getMaxStackSize).orElse(64);
	}

	@Override
	default int getSlots() {
		return 1;
	}

	void onChange(ItemStack old);

	@Override
	default Class<ItemStackNBTData> getDataClass() {
		return ItemStackNBTData.class;
	}
}
