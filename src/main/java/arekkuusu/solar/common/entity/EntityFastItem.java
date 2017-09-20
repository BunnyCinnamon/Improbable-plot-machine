/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by <Arekkuusu> on 08/07/2017.
 * It's distributed as part of Solar.
 */
public class EntityFastItem extends EntityItem {

	private float rest = 0.98F;
	private int despawn;
	private int pickup;

	public EntityFastItem(World world, double x, double y, double z, ItemStack stack) {
		super(world, x, y, z, stack);
		despawn = stack.getItem().getEntityLifespan(stack, world);
		setEntityInvulnerable(true);
		setDefaultPickupDelay();
	}

	public EntityFastItem(EntityItem item) {
		this(item.world, item.posX, item.posY, item.posZ, item.getItem());
	}

	public EntityFastItem(World worldIn) {
		super(worldIn);
		setAgeToCreativeDespawnTime();
		setEntityInvulnerable(true);
	}

	@Override
	public void onUpdate() {
		if(getItem().getItem().onEntityItemUpdate(this)) return;

		if(getItem().isEmpty() || (despawn != -1 && despawn-- == 0)) {
			setDead();
		} else {
			onEntityUpdate();

			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;

			move(MoverType.SELF, motionX, motionY, motionZ);

			if (!hasNoGravity()) {
				motionY -= 0.03999999910593033D;
			} else {
				motionY *= rest;
			}
			motionX *= rest;
			motionZ *= rest;
			updateLogic();
		}
	}

	public void updateLogic() {
		if(!world.isRemote && pickup > 0 && pickup != -1) {
			pickup--;
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player) {
		if(!this.world.isRemote) {
			if(cannotPickup()) return;
			ItemStack itemstack = this.getItem();
			Item item = itemstack.getItem();
			int i = itemstack.getCount();

			int hook = net.minecraftforge.event.ForgeEventFactory.onItemPickup(this, player);
			if(hook < 0) return;

			if(hook == 1 || i <= 0 || player.inventory.addItemStackToInventory(itemstack)) {
				net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerItemPickupEvent(player, this);
				player.onItemPickup(this, i);

				if(itemstack.isEmpty()) {
					this.setDead();
					itemstack.setCount(i);
				}

				player.addStat(StatList.getObjectsPickedUpStats(item), i);
			}
		}
	}

	@Override
	public void move(MoverType type, double x, double y, double z) {
		if(noClip) {
			setEntityBoundingBox(getEntityBoundingBox().offset(x, y, z));
			resetPositionToBB();
		} else {
			world.profiler.startSection("move");

			List<AxisAlignedBB> boxes = world.getCollisionBoxes(this, getEntityBoundingBox().expand(x, y, z));

			if(y != 0.0D) {
				int k = 0;

				for(int l = boxes.size(); k < l; ++k) {
					y = boxes.get(k).calculateYOffset(getEntityBoundingBox(), y);
				}

				setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, y, 0.0D));
			}

			if(x != 0.0D) {
				int j5 = 0;

				for(int l5 = boxes.size(); j5 < l5; ++j5) {
					x = boxes.get(j5).calculateXOffset(getEntityBoundingBox(), x);
				}

				if(x != 0.0D) {
					setEntityBoundingBox(getEntityBoundingBox().offset(x, 0.0D, 0.0D));
				}
			}

			if(z != 0.0D) {
				int k5 = 0;

				for(int i6 = boxes.size(); k5 < i6; ++k5) {
					z = boxes.get(k5).calculateZOffset(getEntityBoundingBox(), z);
				}

				if(z != 0.0D) {
					setEntityBoundingBox(getEntityBoundingBox().offset(0.0D, 0.0D, z));
				}
			}
		}

		world.profiler.endSection();
		world.profiler.startSection("rest");
		resetPositionToBB();
		world.profiler.endSection();
	}

	@Override
	public boolean cannotPickup() {
		return pickup > 0 || pickup == -1;
	}

	@Override
	public void setAgeToCreativeDespawnTime() {
		despawn = 6000;
	}

	@Override
	public void setNoDespawn() {
		despawn = -1;
	}

	@Override
	public void setInfinitePickupDelay() {
		pickup = -1;
	}

	@Override
	public void setPickupDelay(int ticks) {
		pickup = ticks;
	}

	@Override
	public void setNoPickupDelay() {
		pickup = 0;
	}

	@Override
	public void setDefaultPickupDelay() {
		pickup = 20;
	}

	public int getPickupDelay() {
		return pickup;
	}

	public void setMotionRest(float rest) {
		this.rest = rest;
	}

	public void setMotion(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
	}
}
