/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.common.entity.EntityFastItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by <Arekkuusu> on 19/09/2017.
 * It's distributed as part of Solar.
 */
public class TileQSquared extends TileBase implements ITickable {

	public int tick;

	@Override
	public void onLoad() {
		tick =  world.rand.nextInt(15);
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			suspendNearbyItems();
		} else tick++;
	}

	private void suspendNearbyItems() {
		world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(getPos()).grow(5), Entity::isEntityAlive)
				.stream().filter(entity -> !(entity instanceof EntityFastItem)).forEach(this::replace);
	}

	private void replace(EntityItem entity) {
		EntityFastItem item = new EntityFastItem(entity);
		item.setNoDespawn();
		item.setNoGravity(true);
		item.setMotionRest(0.85F);
		item.setMotion(entity.motionX, entity.motionY, entity.motionZ);

		world.spawnEntity(item);
		entity.setDead();
	}

	@Override
	void readNBT(NBTTagCompound cmp) {
		//Get ICED dummy
	}

	@Override
	void writeNBT(NBTTagCompound cmp) {
		//You may now access the ICOSAHEDRON
	}
}
