/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.common.entity.EntityStaticItem;
import arekkuusu.solar.common.lib.LibNames;
import arekkuusu.solar.common.network.PacketHelper;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 14/09/2017.
 * It's distributed as part of Solar.
 */
public class ItemCrystalQuartz extends ItemBase {

	public ItemCrystalQuartz() {
		super(LibNames.CRYSTAL_QUARTZ);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entity) {
		if(!entity.world.isRemote) {
			entity = makeQuantum(entity);
			if(itemRand.nextFloat() < 0.1F) {
				Vector3 from = new Vector3.WrappedVec3d(entity.getPositionVector()).asImmutable();
				Vector3 to = Vector3.rotateRandom().multiply(2).add(from);
				if(isValidSpawn(entity.world, to)) {
					entity.setPositionAndUpdate(to.x(), to.y(), to.z());
					entity.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 0.25F, 0.5F);
				}
			}
		} else {
			Vector3 pos = new Vector3(entity).add(0D, entity.height * 1.75D, 0D);
			Vector3 speedVec = Vector3.rotateRandom().multiply(0.01D);
			FXUtil.spawnLight(entity.world, pos, speedVec, 45, 3F, 0x1BE564, Light.GLOW);
		}
		return false;
	}

	private EntityItem makeQuantum(EntityItem entity) {
		if(entity.isEntityAlive() && entity.getClass().equals(EntityItem.class)) {
			EntityStaticItem item = new EntityStaticItem(entity);
			item.setMotion(entity.motionX, entity.motionY, entity.motionZ);
			item.setNoGravity(true);
			entity.world.spawnEntity(item);
			entity.setDead();
			return item;
		}
		return entity;
	}

	private boolean isValidSpawn(World world, Vector3 to) {
		BlockPos pos = to.toBlockPos();
		return world.isValid(pos) && world.isBlockLoaded(pos) && world.isAirBlock(pos);
	}
}
