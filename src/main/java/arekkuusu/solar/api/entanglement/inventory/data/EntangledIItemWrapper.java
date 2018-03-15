/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.inventory.data;

import arekkuusu.solar.api.entanglement.inventory.EntangledIItemHandler;
import arekkuusu.solar.api.entanglement.inventory.IEntangledIItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 11/08/2017.
 * It's distributed as part of Solar.
 */
public abstract class EntangledIItemWrapper implements IItemHandlerModifiable {

	private final int slots;

	EntangledIItemWrapper(int slots) {
		this.slots = slots;
	}

	public abstract Optional<UUID> getKey();

	protected void onChange(int slot) {}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if(stack.isEmpty()) return ItemStack.EMPTY;
		if(!getKey().isPresent() || isEntangled(stack)) return stack;
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
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
				setStackInSlot(slot, existing);
			}
			onChange(slot);
		}
		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	public final boolean isEntangled(ItemStack stack) {
		if(stack.getItem() instanceof IEntangledIItemStack) {
			Optional<UUID> optional = ((IEntangledIItemStack) stack.getItem()).getKey(stack);
			return optional.isPresent();
		}
		return false;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(amount == 0 || !getKey().isPresent()) return ItemStack.EMPTY;
		ItemStack existing = getStackInSlot(slot);
		if(existing.isEmpty()) {
			return ItemStack.EMPTY;
		}
		int toExtract = Math.min(amount, existing.getMaxStackSize());
		if(existing.getCount() <= toExtract) {
			if(!simulate) {
				setStackInSlot(slot, ItemStack.EMPTY);
				onChange(slot);
			}
			return existing;
		} else {
			if(!simulate) {
				setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				onChange(slot);
			}
			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return getKey().map(uuid -> EntangledIItemHandler.getEntanglementStack(uuid, slot)).orElse(ItemStack.EMPTY);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		getKey().ifPresent(uuid -> EntangledIItemHandler.setEntanglementStack(uuid, stack, slot));
	}

	@Override
	public int getSlotLimit(int slot) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getSlots() {
		return slots;
	}
}
