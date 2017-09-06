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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by <Arekkuusu> on 19/08/2017.
 * It's distributed as part of Solar.
 */
public class EntityQuingentilliardItem extends EntityItem {

	private int coolDown;

	public EntityQuingentilliardItem(World worldIn, double x, double y, double z, ItemStack stack) {
		super(worldIn, x, y, z, stack);
		setDefaultPickupDelay();
		setEntityInvulnerable(true);
		setNoDespawn();
		setNoGravity(true);
	}

	public EntityQuingentilliardItem(World worldIn) {
		super(worldIn);
		setDefaultPickupDelay();
		setEntityInvulnerable(true);
		setNoDespawn();
		setNoGravity(true);
	}

	@Override
	public void onUpdate() {
		if(getItem().getItem().onEntityItemUpdate(this)) return;
		setNoDespawn();

		if(getItem().isEmpty()) {
			setDead();
		} else {
			onEntityUpdate();
			noClip = true;

			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;

			move(MoverType.SELF, motionX, motionY, motionZ);

			float rest = 0.99F;
			motionX *= rest;
			motionY *= rest;
			motionZ *= rest;
		}

		if(coolDown <= 0 || coolDown-- <= 0) {
			Vec3d from = new Vec3d(posX, posY, posZ);
			Vec3d to = from.addVector(motionX * 2, motionY * 2, motionZ * 2);
			RayTraceResult result = world.rayTraceBlocks(from, to);

			if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
				@SuppressWarnings("deprecation")
				float blast = world.getBlockState(result.getBlockPos()).getBlock().getExplosionResistance(null);
				if(blast >= 0F && blast <= 10F) {
					TerrainExplosion explosion = new TerrainExplosion(world, this, 8F);
					explosion.doExplosionA();
					explosion.doExplosionB(true);
					coolDown = 10;
				} else {
					stopMotion();
				}
			}
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player) {
		if(!this.world.isRemote) {
			if(ticksExisted < 20) return;
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

	public void stopMotion() {
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
	}

	public void setMotion(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
	}
}
