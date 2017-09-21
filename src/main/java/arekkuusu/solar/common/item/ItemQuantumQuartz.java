/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.common.entity.EntityQuantumQuartzItem;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.entity.item.EntityItem;

/**
 * Created by <Arekkuusu> on 14/09/2017.
 * It's distributed as part of Solar.
 */
public class ItemQuantumQuartz extends ItemBase {

	public ItemQuantumQuartz() {
		super(LibNames.QUANTUM_QUARTZ);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entity) {
		if(!entity.world.isRemote) makeQuantum(entity);
		return false;
	}

	private void makeQuantum(EntityItem entity) {
		if(entity.isEntityAlive() && entity.getClass().equals(EntityItem.class)) {
			EntityQuantumQuartzItem item = new EntityQuantumQuartzItem(entity);
			item.setMotion(entity.motionX, entity.motionY, entity.motionZ);
			item.setNoGravity(true);
			entity.world.spawnEntity(item);
			entity.setDead();
		}
	}
}
