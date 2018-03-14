/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import arekkuusu.solar.common.network.PacketHelper;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 21/09/2017.
 * It's distributed as part of Solar.
 */
public class EntityCrystalQuartzItem extends EntityStaticItem {

	private Vec3d vec;

	public EntityCrystalQuartzItem(EntityItem item) {
		super(item);
		vec = item.getPositionVector();
		setNoDespawn();
	}

	public EntityCrystalQuartzItem(World worldIn) {
		super(worldIn);
		setNoDespawn();
	}

	@Override
	public void updateLogic() {
		super.updateLogic();
		if(!world.isRemote && rand.nextInt(100) == 0) {
			Vector3 from = Vector3.apply(posX, posY, posZ);
			Vector3 to = Vector3.rotateRandom().multiply(4).add(from);

			BlockPos pos = to.toBlockPos();
			if(!getPosition().equals(pos) && world.isAirBlock(pos) && vec.distanceTo(to.toVec3d()) <= 15) {
				setPositionAndUpdate(to.x(), to.y(), to.z());
				playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1F, 1F);
				//Send teleport effect to clients
				PacketHelper.sendQuartzEffectPacket(this, from.add(0, 0.2D, 0), to.add(0, 0.2D, 0));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(vec != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setDouble("vec_x", vec.x);
			tag.setDouble("vec_y", vec.y);
			tag.setDouble("vec_z", vec.z);

			compound.setTag("vec", tag);
		}
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("vec")) {
			NBTTagCompound tag = compound.getCompoundTag("vec");
			vec = new Vec3d(tag.getDouble("vec_x"), tag.getDouble("vec_y"), tag.getDouble("vec_z"));
		}
		super.readFromNBT(compound);
	}
}
