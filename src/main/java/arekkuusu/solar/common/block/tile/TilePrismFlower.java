/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 14/07/2017.
 * It's distributed as part of Solar.
 */
public class TilePrismFlower extends TileBase implements ITickable {

	private static final Map<EnumFacing, Vec3d> FACING_MAP = ImmutableMap.<EnumFacing, Vec3d>builder()
			.put(EnumFacing.UP, new Vec3d(0.5D, 0.25D, 0.5D))
			.put(EnumFacing.DOWN, new Vec3d(0.5D, 0.75D, 0.5D))
			.put(EnumFacing.NORTH, new Vec3d(0.5D, 0.75D, 0.5D))
			.put(EnumFacing.SOUTH, new Vec3d(0.5D, 0.75D, 0.4D))
			.put(EnumFacing.EAST, new Vec3d(0.4D, 0.75D, 0.5D))
			.put(EnumFacing.WEST, new Vec3d(0.5D, 0.75D, 0.5D))
			.build();
	private final Comparator<EntityLivingBase> comparator = (compared, entity) -> {
		double x = distanceTo(compared);
		double y = distanceTo(entity);

		return x > y ? 1 : x < y ? -1 : 0;
	};
	public float brightness;
	public int tick;

	@Override
	public void update() {
		if(world.isRemote) {
			Optional<EntityLivingBase> optional = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos()).grow(4))
					.stream().sorted(comparator).findFirst();
			if(optional.isPresent()) {
				double distance = distanceTo(optional.get());
				brightness = (float) (1 - (distance / 5)) * 1.5F;
			} else {
				brightness = 0;
			}

			if(tick % 20 == 0 && world.rand.nextInt(10) == 0) {
				spawnParticles();
			}
		}
		tick++;
	}

	private void spawnParticles() {
		Vec3d offset = getOffSet(pos.getX(), pos.getY(), pos.getZ());
		double ytho = world.rand.nextDouble() * 0.2;
		double idk = world.rand.nextDouble() * 0.2;

		world.spawnParticle(EnumParticleTypes.END_ROD, offset.x + ytho, offset.y + (idk * 2), offset.z + idk, 0, 0, 0);
	}

	public double distanceTo(EntityLivingBase entity) {
		double x = pos.getX() + 0.5D - entity.posX;
		double y = (pos.getY() + 0.5D) - (entity.posY + (entity.height / 2));
		double z = pos.getZ() + 0.5D - entity.posZ;

		double squared = x * x + y * y + z * z;
		return Math.sqrt(squared);
	}

	public Vec3d getOffSet(double x, double y, double z) { //Kill me.....
		IBlockState state = world.getBlockState(pos);
		EnumFacing facing = state.getValue(BlockDirectional.FACING);

		return FACING_MAP.get(facing).addVector(x, y, z);
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
