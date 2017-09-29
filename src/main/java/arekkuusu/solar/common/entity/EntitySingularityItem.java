/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import arekkuusu.solar.client.effect.ParticleUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 18/09/2017.
 * It's distributed as part of Solar.
 */
public class EntitySingularityItem extends EntityFastItem {

	private int lifeTime = 10;
	public boolean isBound;

	public EntitySingularityItem(EntityItem item) {
		super(item);
		setPickupDelay(150);
		setNoGravity(true);
		setNoDespawn();
		noClip = true;
	}

	public EntitySingularityItem(World worldIn) {
		super(worldIn);
		setPickupDelay(150);
		setNoGravity(true);
		setNoDespawn();
		noClip = true;
	}

	@Override
	public void updateLogic() {
		super.updateLogic();
		if(!world.isRemote && --lifeTime <= 0 && !isDead) {
			dropSelf();
		}
		if(world.isRemote && rand.nextFloat() < 0.2F) {
			ParticleUtil.spawnTunnelingPhoton(world, posX, posY + 0.21, posZ, 0, 0, 0, 0xFFFFFF, 10, 0.35F);
		}
	}

	public void dropSelf() {
		EntityItem item = new EntityItem(world, posX, posY, posZ, getItem());
		world.spawnEntity(item);
		setDead();
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

	public int getLifeTime() {
		return lifeTime;
	}
}
