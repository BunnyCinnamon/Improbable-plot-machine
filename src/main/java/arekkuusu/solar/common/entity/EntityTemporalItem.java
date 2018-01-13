/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 18/09/2017.
 * It's distributed as part of Solar.
 */
public class EntityTemporalItem extends EntityStaticItem {

	public int lifeTime = 20;

	public EntityTemporalItem(World world, double x, double y, double z, ItemStack stack) {
		super(world, x, y, z, stack);
		setDefaultPickupDelay();
		setNoGravity(true);
		setNoDespawn();
	}

	public EntityTemporalItem(EntityItem item) {
		super(item);
		setDefaultPickupDelay();
		setNoGravity(true);
		setNoDespawn();
		setMotion(item.motionX, item.motionY, item.motionZ);
	}

	public EntityTemporalItem(World worldIn) {
		super(worldIn);
		setDefaultPickupDelay();
		setNoGravity(true);
		setNoDespawn();
	}

	@Override
	public void updateLogic() {
		super.updateLogic();
		if(!world.isRemote && --lifeTime <= 0 && !isDead) {
			dropSelf();
		}
	}

	public void dropSelf() {
		EntityItem item = new EntityItem(world, posX, posY, posZ, getItem());
		world.spawnEntity(item);
		setDead();
	}

	public void setNoClip(boolean clip) {
		this.noClip = clip;
	}
}
