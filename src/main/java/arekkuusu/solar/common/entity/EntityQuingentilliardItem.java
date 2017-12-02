/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import arekkuusu.solar.api.entanglement.quantum.IQuantumStack;
import arekkuusu.solar.api.entanglement.quantum.QuantumHandler;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.item.ItemQuingentilliard;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by <Arekkuusu> on 19/08/2017.
 * It's distributed as part of Solar.
 */
public class EntityQuingentilliardItem extends EntityFastItem {

	public EntityQuingentilliardItem(EntityItem entity) {
		super(entity);
		setMotionRest(0.85F);
		setPickupDelay(60);
	}

	public EntityQuingentilliardItem(World worldIn) {
		super(worldIn);
		setMotionRest(0.85F);
		setPickupDelay(60);
	}

	public void attractItems(World world, ItemStack lookup) {
		List<EntityFastItem> list = getItemsFiltered(world, getEntityBoundingBox().grow(9F), lookup);
		for(EntityFastItem item : list) {
			applyGravity(posX, posY, posZ, item);
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

	public void collectItems(World world, ItemStack stack, UUID uuid, ItemStack lookup) {
		if(stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			ItemQuingentilliard.QuingentilliardStackWrapper handler = (ItemQuingentilliard.QuingentilliardStackWrapper) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(handler == null) return;

			List<EntityFastItem> list = getItemsFiltered(world, getEntityBoundingBox().grow(0.5F), lookup);
			if(!list.isEmpty()) {
				boolean update = false;
				for(EntityItem entity : list) {
					ItemStack inserted = entity.getItem();
					for(int i = 0; i < handler.getSlots(); i++) {
						ItemStack test = handler.insertItemAsync(i, inserted);
						if(test != inserted) {
							entity.setItem(test);
							update = true;
							break;
						}
					}
				}
				if(update) {
					WorldQuantumData.get(world).markDirty();
					WorldQuantumData.syncChanges(uuid);
				}
			}
		}
	}

	private List<EntityFastItem> getItemsFiltered(World world, AxisAlignedBB box, ItemStack lookup) {
		List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, box, Entity::isEntityAlive);
		return list.stream().filter(entity -> {
			ItemStack stack = entity.getItem();

			return !isEntangled(stack) && (lookup.isEmpty() || ItemHandlerHelper.canItemStacksStack(lookup, stack));
		}).map(i -> replace(world, i)).collect(Collectors.toList());
	}

	public final boolean isEntangled(ItemStack stack) {
		if(stack.getItem() instanceof IQuantumStack) {
			Optional<UUID> optional = ((IQuantumStack) stack.getItem()).getKey(stack);
			return optional.isPresent();
		}
		return false;
	}

	private EntityFastItem replace(World world, EntityItem entity) {
		if(entity instanceof EntityFastItem) return (EntityFastItem) entity;
		EntityFastItem item = new EntityFastItem(entity);
		item.setAgeToCreativeDespawnTime();
		item.setNoGravity(true);

		world.spawnEntity(item);
		entity.setDead();
		return item;
	}
}
