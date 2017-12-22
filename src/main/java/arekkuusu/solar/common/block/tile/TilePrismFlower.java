/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.helper.Vector3;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 14/07/2017.
 * It's distributed as part of Solar.
 */
public class TilePrismFlower extends TileBase implements ITickable {

	private static final Map<EnumFacing, Vector3> FACING_MAP = ImmutableMap.<EnumFacing, Vector3>builder()
			.put(EnumFacing.UP, Vector3.create(0.5D, 0.25D, 0.5D))
			.put(EnumFacing.DOWN, Vector3.create(0.5D, 0.75D, 0.5D))
			.put(EnumFacing.NORTH, Vector3.create(0.5D, 0.75D, 0.55D))
			.put(EnumFacing.SOUTH, Vector3.create(0.5D, 0.75D, 0.4D))
			.put(EnumFacing.EAST, Vector3.create(0.4D, 0.75D, 0.5D))
			.put(EnumFacing.WEST, Vector3.create(0.55D, 0.75D, 0.5D))
			.build();
	private final Comparator<EntityLivingBase> comparator = (compared, entity) -> {
		double x = distanceTo(compared);
		double y = distanceTo(entity);

		return Double.compare(x, y);
	};
	public float brightness;
	public int tick;

	@Override
	public void update() {
		if(world.isRemote) {
			Optional<EntityLivingBase> optional = getNearestEntity();
			if(optional.isPresent()) {
				double distance = distanceTo(optional.get());
				brightness = (float) (1 - (distance / 5)) * 1.5F;
			} else {
				brightness = 0;
			}

			if(tick % 20 == 0 && world.rand.nextInt(10) == 0) {
				spawnParticles();
			}
			tick++;
		}
	}

	private Optional<EntityLivingBase> getNearestEntity() {
		return world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()).grow(4))
				.stream().sorted(comparator).findFirst();
	}

	private void spawnParticles() {
		Vector3 offset = getOffSet().add(pos);
		double ytho = world.rand.nextDouble() * 0.2;
		double idk = world.rand.nextDouble() * 0.2;

		world.spawnParticle(EnumParticleTypes.END_ROD, offset.x + ytho, offset.y + (idk * 2), offset.z + idk, 0, 0, 0);
	}

	private double distanceTo(EntityLivingBase entity) {
		double x = pos.getX() + 0.5D - entity.posX;
		double y = (pos.getY() + 0.5D) - (entity.posY + (entity.height / 2));
		double z = pos.getZ() + 0.5D - entity.posZ;

		double squared = x * x + y * y + z * z;
		return Math.sqrt(squared);
	}

	public Vector3 getOffSet() {
		EnumFacing facing = getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
		return FACING_MAP.get(facing).copy();
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		//NO-OP
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		//NO-OP
	}
}
