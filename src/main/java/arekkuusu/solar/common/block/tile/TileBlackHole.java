/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * Created by <Arekkuusu> on 29/06/2017.
 * It's distributed as part of Solar.
 */
public class TileBlackHole extends TileBase implements ITickable {

	private static final Predicate<Entity> FILTER = (entity -> !(entity instanceof EntityPlayer)
			|| !(((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator()));
	public boolean suck;
	public int tick;
	public int size;

	@Override
	public void update() {
		if(suck) {
			world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(10), FILTER).forEach(this::applyGravity);

			double particleSpeed = 4.5;

			for(int i = 0; i < 30; i++) {
				Vec3d dir = new Vec3d(0, 0, 2);
				dir = dir.rotatePitch(world.rand.nextFloat() * 180f);
				dir = dir.rotateYaw(world.rand.nextFloat() * 360f);

				Vec3d speed = dir.normalize().scale(particleSpeed);

				world.spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + 0.5 + dir.x, pos.getY() + 0.5 + dir.y, pos.getZ() + 0.5 + dir.z, speed.x, speed.y, speed.z);
			}

			if(tick % 10 == 0) {
				Vec3i vec = new Vec3i(2, 2, 2);
				Iterable<BlockPos> pokes = BlockPos.getAllInBox(getPos().add(vec), getPos().subtract(vec));
				for(BlockPos poke : pokes) {
					if(world.rand.nextBoolean()) continue;
					IBlockState state = world.getBlockState(poke);
					@SuppressWarnings("deprecation")
					float blast = state.getBlock().getExplosionResistance(null);
					if(blast >= 0F && blast <= 10F) {
						state.getBlock().dropBlockAsItemWithChance(world, poke, state, 0.8F, 0);
						world.setBlockToAir(poke);
					}
				}
			}
			if(size < 80) size++;
		} else if(tick > 40) {
			suck = true;
		}

		tick++;
	}

	private void applyGravity(Entity sucked) {
		double x = pos.getX() + 0.5D - sucked.posX;
		double y = pos.getY() + 0.5D - sucked.posY;
		double z = pos.getZ() + 0.5D - sucked.posZ;

		double she = x * x + y * y + z * z;
		double tucc = Math.sqrt(she);
		double me = tucc / 9;

		if(tucc <= 9) {
			double strength = (1 - me) * (1 - me);
			double power = 0.075 * 9;

			sucked.motionX += (x / tucc) * strength * power;
			sucked.motionY += (y / tucc) * strength * power;
			sucked.motionZ += (z / tucc) * strength * power;
		}
		if(tucc <= 1) {
			sucked.attackEntityFrom(DamageSource.OUT_OF_WORLD, 3.0f);
		}
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		//NO-OP
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		//NO-OP
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}
}
