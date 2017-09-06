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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by <Arekkuusu> on 08/07/2017.
 * It's distributed as part of Solar.
 */
public class EntitySpecialItem extends EntityItem {

	public boolean isBound;

	public EntitySpecialItem(EntityItem item) {
		super(item.world, item.posX, item.posY, item.posZ, item.getItem());
		setInfinitePickupDelay();
		setEntityInvulnerable(true);
		setNoDespawn();
		setNoGravity(true);
	}

	public EntitySpecialItem(World worldIn) {
		super(worldIn);
		setInfinitePickupDelay();
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
			if(!world.isRemote && !isBound) {
				motionX = 0;
				motionY = 0;
				motionZ = 0;
			}
			noClip = true;

			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;

			move(MoverType.SELF, motionX, motionY, motionZ);

			float rest = 0.98F;
			motionX *= rest;
			motionY *= rest;
			motionZ *= rest;
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
}
