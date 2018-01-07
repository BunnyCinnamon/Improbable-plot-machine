/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.entity.EntityFastItem;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by <Snack> on 06/01/2018.
 * It's distributed as part of Solar.
 */
public class TileVacuumConveyor extends TileBase implements ITickable {

	private Pair<IItemHandler, ISidedInventory> to;
	private ItemStack lookup = ItemStack.EMPTY;

	public TileVacuumConveyor(EnumFacing except) {
		this.to = getInventory(except);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			absorb();
		} else {
			ParticleUtil.spawnSquared(world, Vector3.create(pos).add(0.5D), Vector3.ImmutableVector3.NULL, 0x000000, 10, 2F);
		}
	}

	private void absorb() {
		if(to.getValue() != null) {
			attractItems();
			IItemHandler handler = to.getKey();
			ISidedInventory tile = to.getValue();
			world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos()).grow(0.5D)).forEach(e -> {
				for(int slot = 0; slot < handler.getSlots(); slot++) {
					ItemStack inSlot = handler.getStackInSlot(slot);
					ItemStack in = e.getItem();
					if(tile == null || tile.canInsertItem(slot, in, getFacing())) {
						if(inSlot.isEmpty() || (ItemHandlerHelper.canItemStacksStack(inSlot, in) && (inSlot.getCount() < inSlot.getMaxStackSize() && inSlot.getCount() < handler.getSlotLimit(slot)))) {
							ItemStack out = handler.insertItem(slot, in, false);
							if(out != in) {
								e.setItem(out);
								break;
							}
						}
					}
				}
			});
		}
	}

	public void attractItems() {
		List<EntityFastItem> list = getItemsFiltered(new AxisAlignedBB(getPos()).grow(10F));
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
			double power = 0.075D * 2D;

			sucked.motionX += (x / sqrt) * strength * power;
			sucked.motionY += (y / sqrt) * strength * power;
			sucked.motionZ += (z / sqrt) * strength * power;
		}
	}

	private List<EntityFastItem> getItemsFiltered(AxisAlignedBB box) {
		List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, box, Entity::isEntityAlive);
		return list.stream().filter(entity -> {
			ItemStack stack = entity.getItem();

			return lookup.isEmpty() || ItemHandlerHelper.canItemStacksStack(lookup, stack);
		}).map(this::replace).collect(Collectors.toList());
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

	public void updateInventoryAccess() {
		this.to = getInventory(getFacing());
	}

	private Pair<IItemHandler, ISidedInventory> getInventory(EnumFacing facing) {
		BlockPos target = getPos().offset(facing);
		if(world.isBlockLoaded(target, false)) {
			TileEntity tile = world.getTileEntity(target);
			if(tile != null) {
				IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				if(handler == null) handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				return Pair.of(handler, tile instanceof ISidedInventory ? (ISidedInventory) tile : null);
			}
		}
		return Pair.of(null, null);
	}

	private EnumFacing getFacing() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public void setLookup(ItemStack stack) {
		lookup = stack;
		updatePosition(world, pos);
		markDirty();
	}

	public ItemStack getLookup() {
		return lookup;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		lookup = new ItemStack((NBTTagCompound) compound.getTag("lookup"));
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag("lookup", lookup.writeToNBT(new NBTTagCompound()));
	}
}
