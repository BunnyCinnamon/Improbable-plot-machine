/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.quantum.IQuantumStack;
import arekkuusu.solar.api.entanglement.quantum.data.QuantumTileWrapper;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.entity.EntityFastItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by <Arekkuusu> on 19/12/2017.
 * It's distributed as part of Solar.
 */
public class TileQuingentilliard extends TileQuantumBase<QuantumTileWrapper> implements ITickable {

	public static final int SLOTS = 16;
	private ItemStack lookup = ItemStack.EMPTY;
	public int tick;

	@Override
	public QuantumTileWrapper createHandler() {
		return new QuingentilliardDataHandlerImpl(this, SLOTS);
	}

	@Override
	public void handleItemTransfer(EntityPlayer player, EnumHand hand) {
		Solar.LOG.warn("[Quingentilliard] You cannot interact with it!");
	}

	@Override
	public void takeItem(EntityPlayer player) {
		Solar.LOG.warn("[Quingentilliard] You cannot take items from it!");
	}

	public void setLookup(ItemStack stack) {
		lookup = stack;
		markDirty();
		updatePosition(world, pos);
	}

	public ItemStack getLookup() {
		return lookup;
	}

	@Override
	public void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		lookup = new ItemStack((NBTTagCompound) compound.getTag("lookup"));
	}

	@Override
	public void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		compound.setTag("lookup", lookup.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void update() {
		if(!world.isRemote && !lookup.isEmpty() && getKey().isPresent()) {
			attractItems();
			collectItems();
		}
		if(world.isRemote) tick++;
	}

	public void attractItems() {
		List<EntityFastItem> list = getItemsFiltered(new AxisAlignedBB(getPos()).grow(9F));
		for(EntityFastItem item : list) {
			applyGravity(getPos().getX(), getPos().getY(), getPos().getZ(), item);
		}
	}

	private void applyGravity(double x, double y, double z, Entity sucked) {
		x += 0.5D - sucked.posX;
		y += 0.5D - sucked.posY;
		z += 0.5D - sucked.posZ;

		double sqrt = Math.sqrt(x * x + y * y + z * z);
		double v = sqrt / 9;

		if(sqrt <= 9) {
			double strength = (1 - v) * (1 - v);
			double power = 0.075D * 1.5D;

			sucked.motionX += (x / sqrt) * strength * power;
			sucked.motionY += (y / sqrt) * strength * power;
			sucked.motionZ += (z / sqrt) * strength * power;
		}
	}

	public void collectItems() {
		if(hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			QuingentilliardDataHandlerImpl handler = (QuingentilliardDataHandlerImpl) getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(handler == null) return;

			List<EntityFastItem> list = getItemsFiltered(new AxisAlignedBB(getPos()).grow(0.5F));
			if(!list.isEmpty()) {
				for(EntityItem entity : list) {
					ItemStack inserted = entity.getItem();
					for(int i = 0; i < handler.getSlots(); i++) {
						ItemStack test = handler.insert(i, inserted, false);
						if(test != inserted) {
							entity.setItem(test);
							break;
						}
					}
				}
			}
		}
	}

	private List<EntityFastItem> getItemsFiltered(AxisAlignedBB box) {
		List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, box, Entity::isEntityAlive);
		return list.stream().filter(entity -> {
			ItemStack stack = entity.getItem();

			return !isEntangled(stack) && ItemHandlerHelper.canItemStacksStack(lookup, stack);
		}).map(this::replace).collect(Collectors.toList());
	}

	private boolean isEntangled(ItemStack stack) {
		if(stack.getItem() instanceof IQuantumStack) {
			Optional<UUID> optional = ((IQuantumStack) stack.getItem()).getKey(stack);
			return optional.isPresent();
		}
		return false;
	}

	private EntityFastItem replace(EntityItem entity) {
		if(entity instanceof EntityFastItem) return (EntityFastItem) entity;
		EntityFastItem item = new EntityFastItem(entity);
		item.setAgeToCreativeDespawnTime();
		item.setNoGravity(true);

		world.spawnEntity(item);
		entity.setDead();
		return item;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass <= 1;
	}

	private static class QuingentilliardDataHandlerImpl extends QuantumTileWrapper<TileQuingentilliard> {

		public QuingentilliardDataHandlerImpl(TileQuingentilliard tile, int slots) {
			super(tile, slots);
		}

		public ItemStack insert(int slow, ItemStack stack, boolean simulate) {
			return super.insertItem(slow, stack, simulate);
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			tile.setLookup(stack);
			return stack;
		}
	}
}
