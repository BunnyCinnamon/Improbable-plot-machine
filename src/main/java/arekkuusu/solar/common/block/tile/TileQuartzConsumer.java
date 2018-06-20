/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/30/2018.
 * It's distributed as part of Solar.
 */
public class TileQuartzConsumer extends TileBase {

	private final InventoryWrapper handler;

	public TileQuartzConsumer() {
		this.handler = new InventoryWrapper(this);
	}

	public boolean consume(ItemStack stack) {
		if(!stack.isEmpty() && stack.getItem() == ModItems.CRYSTAL_QUARTZ) {
			if(!world.isRemote) {
				markDirty();
				sync();
				handler.setStackInSlot(0, stack.copy());
				stack.shrink(1);
			}
			return true;
		}
		return false;
	}

	public boolean getHasItem() {
		return !handler.stack.isEmpty();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: null;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasKey("stack")) {
			handler.stack = new ItemStack(compound.getCompoundTag("stack"));
		}
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		if(!handler.stack.isEmpty()) {
			NBTTagCompound tag = new NBTTagCompound();
			handler.stack.writeToNBT(tag);
			compound.setTag("stack", tag);
		}
	}

	public static class InventoryWrapper implements IItemHandlerModifiable {

		private final TileQuartzConsumer tile;
		private ItemStack stack = ItemStack.EMPTY;

		private InventoryWrapper(TileQuartzConsumer tile) {
			this.tile = tile;
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			this.stack = stack;
			this.stack.setCount(1);
		}

		@Override
		public int getSlots() {
			return 1;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return stack;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(!stack.isEmpty() && stack.getItem() == ModItems.CRYSTAL_QUARTZ) {
				if(!simulate) setStackInSlot(0, stack.copy());
				return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
			}
			return stack;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(amount > 0 && !stack.isEmpty()) {
				if(!simulate) this.stack = ItemStack.EMPTY;
				return stack.copy();
			}
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	}
}
