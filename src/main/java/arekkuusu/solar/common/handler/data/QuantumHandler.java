/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.quantum.EntanglementHelper;
import arekkuusu.solar.api.quantum.IEntangledStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 11/08/2017.
 * It's distributed as part of Solar.
 */
public abstract class QuantumHandler implements IItemHandlerModifiable {

	private final int slots;

	protected QuantumHandler(int slots) {
		this.slots = slots;
	}

	@Nullable
	public abstract UUID getKey();

	protected void onChange(int slot) {}

	public boolean assertSafety(ItemStack stack) {
		return !(stack.getItem() instanceof IEntangledStack) || !((IEntangledStack) stack.getItem()).getKey(stack).isPresent();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(getKey() == null) return ItemStack.EMPTY;

		return EntanglementHelper.getQuantumStack(getKey(), slot);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		EntanglementHelper.setQuantumStack(getKey(), stack, slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if(stack.isEmpty()) return ItemStack.EMPTY;

		if(getKey() == null || !assertSafety(stack)) return stack;

		ItemStack existing = EntanglementHelper.getQuantumStack(getKey(), slot);

		int limit = stack.getMaxStackSize();

		if(!existing.isEmpty()) {
			if(!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
				return stack;
			}
			limit -= existing.getCount();
		}

		if(limit <= 0) {
			return stack;
		}

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

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(amount == 0 || getKey() == null) return ItemStack.EMPTY;

		ItemStack existing = EntanglementHelper.getQuantumStack(getKey(), slot);

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
	public int getSlotLimit(int slot) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getSlots() {
		return slots;
	}
}
