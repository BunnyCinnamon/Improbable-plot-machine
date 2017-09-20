/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.common.entity.EntityFastItem;
import arekkuusu.solar.common.lib.LibNames;
import arekkuusu.solar.common.network.PacketHandler;
import arekkuusu.solar.common.network.QTeleportEffectMessage;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
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
		if(!world.isRemote && !(entity instanceof EntityFastItem) && itemRand.nextInt(100) == 0) {
			Vector3 from = new Vector3(entity.posX, entity.posY, entity.posZ);
			Vector3 to = Vector3.getRandomVec().add(from);

			BlockPos pos = to.toBlockPos();
			if(!entity.getPosition().equals(pos) && world.isAirBlock(pos)) {
				entity.setPositionAndUpdate(to.x, to.y, to.z);
				entity.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1F, 1F);
				//Send teleport effect to clients
				QTeleportEffectMessage teleport = new QTeleportEffectMessage(from, to);
				PacketHandler.sendToAllAround(teleport, PacketHandler.fromWorldPos(world, pos, 25));
			}
		}
		return false;
	}
}
