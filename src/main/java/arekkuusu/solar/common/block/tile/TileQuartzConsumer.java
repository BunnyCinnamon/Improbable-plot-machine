/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.energy.data.ILumen;
import arekkuusu.solar.common.entity.EntityLumen;
import arekkuusu.solar.common.handler.data.ModCapability;
import arekkuusu.solar.common.item.ModItems;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/30/2018.
 * It's distributed as part of Solar.
 */
public class TileQuartzConsumer extends TileBase implements ITickable {

	private final InventoryWrapper handler = new InventoryWrapper();

	@Override
	public void update() {
		if(!world.isRemote && world.rand.nextFloat() < 0.1F && !handler.stack.isEmpty()) {
			ILumen ilumen = handler.stack.getCapability(ModCapability.NEUTRON_CAPABILITY, null);
			assert ilumen != null;
			int drain = world.rand.nextInt(15);
			if(drain > 0 && ilumen.get() >= drain && ilumen.drain(drain, true) == drain) {
				EntityLumen lumen = EntityLumen.spawn(world, new Vector3.WrappedVec3i(getPos()).asImmutable().add(0.5D), drain);
				Quat x = Quat.fromAxisAngle(Vector3.Forward(), (world.rand.nextFloat() * 2F - 1F) * 75F);
				Quat z = Quat.fromAxisAngle(Vector3.Right(), (world.rand.nextFloat() * 2F - 1F) * 75F);
				Vector3 vec = Vector3.Up().rotate(x.multiply(z)).multiply(0.1D);
				lumen.setMotion(vec);
			} else {
				handler.stack = ItemStack.EMPTY;
				markDirty();
				sync();
			}
		}
	}

	public boolean consume(ItemStack stack) {
		if(!stack.isEmpty() && stack.getItem() == ModItems.CRYSTAL_QUARTZ && stack.hasCapability(ModCapability.NEUTRON_CAPABILITY, null)) {
			if(!world.isRemote) {
				handler.stack = stack.copy();
				stack.shrink(1);
				markDirty();
				sync();
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
		} else handler.stack = ItemStack.EMPTY;
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		if(!handler.stack.isEmpty()) {
			NBTTagCompound tag = new NBTTagCompound();
			handler.stack.writeToNBT(tag);
			compound.setTag("stack", tag);
		}
	}

	public static class InventoryWrapper implements IItemHandler {

		private ItemStack stack = ItemStack.EMPTY;

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
				if(!simulate) {
					this.stack = stack.copy();
					this.stack.setCount(1);
				}
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
