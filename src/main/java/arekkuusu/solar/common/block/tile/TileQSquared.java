/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.entity.EntityTemporalItem;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 19/09/2017.
 * It's distributed as part of Solar.
 */
public class TileQSquared extends TileBase implements ITickable {

	private final List<EntityTemporalItem> items = Lists.newArrayList();
	private final IItemHandler handler;
	public int tick;

	public TileQSquared() {
		this.handler = new QSquaredItemHandler(this);
	}

	@Override
	public void onLoad() {
		tick = world.rand.nextInt(15);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			suspendNearbyItems();
			items.removeIf(item -> item.isDead);
		} else tick++;
	}

	private void suspendNearbyItems() {
		world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos()).grow(5), Entity::isEntityAlive).stream()
				.map(this::map)
				.forEach(item -> {
					if(!items.contains(item)) items.add(item);
				});
	}

	private EntityTemporalItem map(EntityItem entity) {
		if(entity instanceof EntityTemporalItem) {
			((EntityTemporalItem) entity).lifeTime = 10;
			return (EntityTemporalItem) entity;
		}
		EntityTemporalItem item = new EntityTemporalItem(entity);
		item.setMotion(entity.motionX, entity.motionY, entity.motionZ);
		item.setMotionRest(0.85F);
		world.spawnEntity(item);
		entity.setDead();
		return item;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		//Whomst'v read my NBT?
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		//Whomst'v write my NBT?
	}

	public static class QSquaredItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {

		private final TileQSquared tile;

		private QSquaredItemHandler(TileQSquared tile) {
			this.tile = tile;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return new NBTTagCompound();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) { }

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			EntityTemporalItem item = tile.items.get(slot);
			item.setItem(stack);
		}

		@Override
		public int getSlots() {
			return tile.items.size();
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return tile.items.get(slot).getItem();
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			if(stack.isEmpty()) return ItemStack.EMPTY;
			ItemStack existing = tile.items.get(slot).getItem();
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
				if(!existing.isEmpty()) {
					existing.grow(reachedLimit ? limit : stack.getCount());
					tile.items.get(slot).setItem(existing);
				}
			}
			return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(amount == 0) return ItemStack.EMPTY;
			ItemStack existing = tile.items.get(slot).getItem();
			if(existing.isEmpty()) {
				return ItemStack.EMPTY;
			}
			int toExtract = Math.min(amount, existing.getMaxStackSize());
			if(existing.getCount() <= toExtract) {
				if(!simulate) {
					tile.items.get(slot).setItem(ItemStack.EMPTY);
				}
				return existing;
			} else {
				if(!simulate) {
					tile.items.get(slot).setItem(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				}
				return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
			}
		}

		@Override
		public int getSlotLimit(int slot) {
			return tile.items.size() > slot ? tile.items.get(slot).getItem().getMaxStackSize() : 0;
		}
	}
}
