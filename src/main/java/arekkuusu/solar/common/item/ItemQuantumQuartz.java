/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.common.entity.EntitySpecialItem;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		World world = entity.world;
		entity.setNoGravity(true);
		if(!world.isRemote && !(entity instanceof EntitySpecialItem) && itemRand.nextInt(50) == 0) {
			Vector3 vec = getRandomVec(new Vector3(entity.posX, entity.posY, entity.posZ));
			BlockPos pos = vec.toShit();
			if(!entity.getPosition().equals(pos) && world.isAirBlock(pos)) {
				entity.setPositionAndUpdate(vec.x, vec.y, vec.z);
				entity.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1F, 1F);
			}
		}
		return false;
	}

	private Vector3 getRandomVec(Vector3 pos) {
		for(int j = 0, randomized = itemRand.nextInt(6); j < randomized; j++) {
			EnumFacing facing = EnumFacing.values()[itemRand.nextInt(5)];
			pos.offset(facing, itemRand.nextFloat() * 3);
		}

		return pos;
	}
}
