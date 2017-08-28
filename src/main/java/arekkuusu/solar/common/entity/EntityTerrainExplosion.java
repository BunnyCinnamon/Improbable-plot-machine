/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Created by <Arekkuusu> on 19/08/2017.
 * It's distributed as part of Solar.
 */
public class EntityTerrainExplosion extends Explosion {

	private final World world;
	private final Entity exploder;
	private final float size;

	public EntityTerrainExplosion(World world, Entity entity, float size) {
		super(world, entity, entity.posX, entity.posY, entity.posZ, size, false, true);
		this.world = world;
		this.exploder = entity;
		this.size = size;
	}

	@Override
	public void doExplosionA() {
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
						double d4 = getPosition().x;
						double d6 = getPosition().y;
						double d8 = getPosition().z;

						for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
							BlockPos blockpos = new BlockPos(d4, d6, d8);
							IBlockState iblockstate = this.world.getBlockState(blockpos);

							if(iblockstate.getMaterial() != Material.AIR) {
								float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(world, blockpos, (Entity) null, this);
								f -= (f2 + 0.3F) * 0.3F;
							}

							if(f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, iblockstate, f))) {
								set.add(blockpos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}
				}
			}
		}

		getAffectedBlockPositions().addAll(set);
	}

	@Override
	public void doExplosionB(boolean spawnParticles) {
		world.playSound(null, getPosition().x, getPosition().y, getPosition().z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

		for(BlockPos blockpos : getAffectedBlockPositions()) {
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();

			if(spawnParticles && world.rand.nextInt(5) == 0) {
				double d0 = (double) ((float) blockpos.getX() + world.rand.nextFloat());
				double d1 = (double) ((float) blockpos.getY() + world.rand.nextFloat());
				double d2 = (double) ((float) blockpos.getZ() + world.rand.nextFloat());
				double d3 = d0 - getPosition().x;
				double d4 = d1 - getPosition().y;
				double d5 = d2 - getPosition().z;
				double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
				d3 = d3 / d6;
				d4 = d4 / d6;
				d5 = d5 / d6;
				double d7 = 0.5D / (d6 / (double) size + 0.1D);
				d7 = d7 * (double) (world.rand.nextFloat() * world.rand.nextFloat() + 0.3F);
				d3 = d3 * d7;
				d4 = d4 * d7;
				d5 = d5 * d7;
				world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + getPosition().x) / 2.0D, (d1 + getPosition().y) / 2.0D, (d2 + getPosition().z) / 2.0D, d3, d4, d5);
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
			}

			if(!world.isRemote && iblockstate.getMaterial() != Material.AIR) {
				if(block.canDropFromExplosion(this)) {
					block.dropBlockAsItemWithChance(world, blockpos, world.getBlockState(blockpos), 1.0F / size, 0);
				}

				block.onBlockExploded(world, blockpos, this);
			}
		}
	}
}
