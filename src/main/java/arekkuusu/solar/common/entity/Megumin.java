/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.entity;

import com.google.common.collect.Sets;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

/**
 * Created by <Arekkuusu> on 01/11/2017.
 * It's distributed as part of Solar.
 */
//For our crimson demon lord Megumin!
public class Megumin {

	private final Explosion dummy;
	private final World world;
	private final Vector3 pos;
	private boolean damageEntities;
	private float size;

	private Megumin(World world, Vector3 pos, float size, boolean damageEntities) {
		dummy = new Explosion(world, null, pos.x(), pos.y(), pos.z(), size, false, false);
		this.world = world;
		this.pos = pos;
		this.size = size;
		this.damageEntities = damageEntities;
	}

	public static Megumin chant(World world, Vector3 pos, float size, boolean damageEntities) {
		return new Megumin(world, pos, size, damageEntities);
	}

	public void explosion() {
		world.playSound(null, pos.x(), pos.y(), pos.z(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

		Set<BlockPos> set = Sets.newHashSet();

		for(int j = 0; j < 16; ++j) {
			for(int k = 0; k < 16; ++k) {
				for(int l = 0; l < 16; ++l) {
					if(j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
						double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
						double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
						double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 = d0 / d3;
						d1 = d1 / d3;
						d2 = d2 / d3;
						float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
						double d4 = pos.x();
						double d6 = pos.y();
						double d8 = pos.z();

						for(; f > 0.0F; f -= 0.22500001F) {
							BlockPos pos = new BlockPos(d4, d6, d8);
							IBlockState state = this.world.getBlockState(pos);

							if(state.getMaterial() != Material.AIR) {
								float f2 = state.getBlock().getExplosionResistance(world, pos, null, dummy);
								f -= (f2 + 0.3F) * 0.3F;
							}

							if(f > 0.0F) {
								set.add(pos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}
				}
			}
		}

		for(BlockPos blockpos : set) {
			double d0 = (double) ((float) blockpos.getX() + this.world.rand.nextFloat());
			double d1 = (double) ((float) blockpos.getY() + this.world.rand.nextFloat());
			double d2 = (double) ((float) blockpos.getZ() + this.world.rand.nextFloat());
			double d3 = d0 - pos.x();
			double d4 = d1 - pos.y();
			double d5 = d2 - pos.z();
			double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
			d3 = d3 / d6;
			d4 = d4 / d6;
			d5 = d5 / d6;
			double d7 = 0.5D / (d6 / (double) this.size + 0.1D);
			d7 = d7 * (double) (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
			d3 = d3 * d7;
			d4 = d4 * d7;
			d5 = d5 * d7;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + pos.x()) / 2.0D, (d1 + pos.y()) / 2.0D, (d2 + pos.z()) / 2.0D, d3, d4, d5);
			this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
		}

		if(damageEntities) {
			size *= 2.0F;
			int minX = MathHelper.floor(pos.x() - size - 1.0D);
			int minY = MathHelper.floor(pos.x() + size + 1.0D);
			int minZ = MathHelper.floor(pos.y() - size - 1.0D);
			int maxX = MathHelper.floor(pos.y() + size + 1.0D);
			int maxY = MathHelper.floor(pos.z() - size - 1.0D);
			int maxZ = MathHelper.floor(pos.z() + size + 1.0D);
			List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) minX, (double) minZ, (double) maxY, (double) minY, (double) maxX, (double) maxZ));
			net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(world, dummy, list, size);
			Vec3d vec3d = new Vec3d(pos.x(), pos.y(), pos.z());

			for(Entity entity : list) {
				if(!entity.isImmuneToExplosions()) {
					double distance = entity.getDistance(pos.x(), pos.y(), pos.z()) / (double) size;

					if(distance <= 1.0D) {
						double x = entity.posX - pos.x();
						double y = entity.posY + (double) entity.getEyeHeight() - pos.y();
						double z = entity.posZ - pos.z();
						double sqrt = (double) MathHelper.sqrt(x * x + y * y + z * z);

						if(sqrt != 0.0D) {
							x = x / sqrt;
							y = y / sqrt;
							z = z / sqrt;
							double density = (double) this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
							double knockback = (1.0D - distance) * density;
							entity.attackEntityFrom(DamageSource.causeExplosionDamage(dummy), (float) ((int) ((knockback * knockback + knockback) / 2.0D * 7.0D * size + 1.0D)));

							if(entity instanceof EntityLivingBase) {
								knockback = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, knockback);
							}

							entity.motionX += x * knockback;
							entity.motionY += y * knockback;
							entity.motionZ += z * knockback;
						}
					}
				}
			}
		}
	}
}
