/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.entity.EntitySpecialItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Solar.
 */
public class TileSingularity extends TileBase implements ITickable {

	private final List<EntitySpecialItem> list = new ArrayList<>();
	private final SingularityItemHandler handler;
	private final String TAG_TIME = "time";
	private final String TAG_ORBIT = "orbit";
	private final String TAG_ANGLE = "angle";
	private final String TAG_ROTATION = "rotation";
	public int tick;

	public TileSingularity() {
		handler = new SingularityItemHandler(this);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			purgeItems();
			world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(6)).forEach(this::applyGravity);
			orbitAll();
		} else if(world.rand.nextInt(10) == 0) {
			double x = pos.getX() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double y = pos.getY() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double z = pos.getZ() + 0.5D + (world.rand.nextDouble() * 2F - 1D);
			double speed = 0.01D;

			ParticleUtil.spawnNeutronBlast(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, speed, x, y, z, 0xFFFFFF, 0.1F, false);
		}
		tick++;
	}

	private void applyGravity(EntityItem entity) {
		if(entity.isDead || entity.getItem().isEmpty()) return;
		if(!(entity instanceof EntitySpecialItem)) {
			double x = pos.getX() + 0.5D - entity.posX;
			double y = pos.getY() + 0.5D - entity.posY;
			double z = pos.getZ() + 0.5D - entity.posZ;

			double git = x * x + y * y + z * z;
			double gud = Math.sqrt(git);

			if(gud <= 5) {
				EntitySpecialItem special = new EntitySpecialItem(entity);
				world.spawnEntity(special);

				entity.setDead();
			}
			return;
		}

		EntitySpecialItem item = (EntitySpecialItem) entity;
		bindItem(item);
	}

	private void orbitAll() {
		for(EntitySpecialItem item : list) {
			int orbitTime = getOrbitTime(item);
			double radius = getOrbit(item);
			int angle = orbitTime % 360;

			double xTarget = pos.getX() + 0.5D + Math.cos(angle * 5 * Math.PI / 180F) * radius;
			double yTarget = pos.getY() + getYOffset(orbitTime, getAngle(item));
			double zTarget = pos.getZ() + 0.5D + Math.sin(angle * 5 * Math.PI / 180F) * radius;

			Vec3d targetVec = new Vec3d(xTarget, yTarget, zTarget);
			Vec3d currentVec = new Vec3d(item.posX, item.posY - item.height, item.posZ);
			Vec3d moveVector = targetVec.subtract(currentVec);

			item.motionX = moveVector.x;
			item.motionY = moveVector.y;
			item.motionZ = moveVector.z;

			incrementOrbitTime(item);
		}
	}

	private double getYOffset(int age, double max) {
		double speed = 4;
		double angle = 0;

		double toDegrees = Math.PI / 180D;
		angle += speed * age;
		if (angle > 360D) angle -= 360D;

		return max * Math.sin(angle * toDegrees);
	}

	private int getOrbitTime(EntitySpecialItem item) {
		NBTTagCompound cmp = item.getEntityData();
		if(cmp.hasKey(TAG_TIME))
			return cmp.getInteger(TAG_TIME);
		else return 0;
	}

	private void incrementOrbitTime(EntitySpecialItem item) {
		NBTTagCompound cmp = item.getEntityData();
		int time = getOrbitTime(item);
		cmp.setInteger(TAG_TIME, time + getRotation(item));
	}

	private void setOrbit(EntitySpecialItem item, double orbit) {
		NBTTagCompound tag = item.getEntityData();
		tag.setDouble(TAG_ORBIT, orbit);
	}

	private double getOrbit(EntitySpecialItem item) {
		NBTTagCompound tag = item.getEntityData();
		return tag.getDouble(TAG_ORBIT);
	}

	private void setAngle(EntitySpecialItem item, double angle) {
		NBTTagCompound tag = item.getEntityData();
		tag.setDouble(TAG_ANGLE, angle);
	}

	private double getAngle(EntitySpecialItem item) {
		NBTTagCompound tag = item.getEntityData();
		return tag.getDouble(TAG_ANGLE);
	}

	private void setRotation(EntitySpecialItem item, int rotate) {
		NBTTagCompound tag = item.getEntityData();
		tag.setInteger(TAG_ROTATION, rotate);
	}

	private int getRotation(EntitySpecialItem item) {
		NBTTagCompound tag = item.getEntityData();
		return tag.getInteger(TAG_ROTATION);
	}

	private void purgeItems() {
		List<EntitySpecialItem> removed = list.stream()
				.filter(item -> item.isDead || item.getItem().isEmpty())
				.collect(Collectors.toList());

		list.removeAll(removed);
	}

	public void bindItem(EntitySpecialItem item) {
		if(!world.isRemote && !list.contains(item) && !item.isBound) {
			double x = pos.getX() + 0.5D - item.posX;
			double y = pos.getY() + 0.5D - item.posY;
			double z = pos.getZ() + 0.5D - item.posZ;

			double git = x * x + y * y + z * z;
			double gud = Math.sqrt(git);

			NBTTagCompound tag = item.getEntityData();
			if(!tag.hasKey(TAG_ORBIT))
				setOrbit(item, gud);
			if(!tag.hasKey(TAG_ANGLE))
				setAngle(item, (y * y) / y);
			if(!tag.hasKey(TAG_ROTATION))
				setRotation(item, y >= 0 ? 1 : -1);

			item.isBound = true;
			list.add(item);
		}
	}

	public void unbindItem(EntitySpecialItem item) {
		if(!world.isRemote) {
			EntityItem drop = new EntityItem(world, item.posX, item.posY, item.posZ, item.getItem());
			drop.setAgeToCreativeDespawnTime();
			world.spawnEntity(drop);

			item.setDead();
			list.remove(item);
		}
	}

	public void removeAll() {
		if(!world.isRemote) {
			for(EntityItem item : list) {
				if(!item.isDead && !item.getItem().isEmpty()) {
					EntityItem drop = new EntityItem(world, item.posX, item.posY, item.posZ, item.getItem());
					drop.setAgeToCreativeDespawnTime();
					world.spawnEntity(drop);
					item.setDead();
				}
			}
			list.clear();
		}
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		// ohno.png
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		// yudodis.png
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
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	private static class SingularityItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {

		private final TileSingularity tile;

		SingularityItemHandler(TileSingularity tile) {
			this.tile = tile;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			//Giggity le_lenny.png
			return new NBTTagCompound();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			//Aesthethicc kanna_lewd.png
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			EntitySpecialItem item = tile.list.get(slot);
			item.setItem(stack);
		}

		@Override
		public int getSlots() {
			return tile.list.size();
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return tile.list.get(slot).getItem();
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			if(stack.isEmpty()) return ItemStack.EMPTY;

			ItemStack existing = tile.list.get(slot).getItem();

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
					tile.list.get(slot).setItem(existing);
				}
			}

			return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(amount == 0) return ItemStack.EMPTY;

			ItemStack existing = tile.list.get(slot).getItem();

			if(existing.isEmpty()) {
				return ItemStack.EMPTY;
			}

			int toExtract = Math.min(amount, existing.getMaxStackSize());

			if(existing.getCount() <= toExtract) {
				if(!simulate) {
					tile.list.get(slot).setItem(ItemStack.EMPTY);
				}
				return existing;
			} else {
				if(!simulate) {
					tile.list.get(slot).setItem(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				}

				return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
			}
		}

		@Override
		public int getSlotLimit(int slot) {
			return Integer.MAX_VALUE;
		}
	}
}
