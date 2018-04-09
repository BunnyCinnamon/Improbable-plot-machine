/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.fluid;

import arekkuusu.solar.common.block.BlockBase;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 4/8/2018.
 * It's distributed as part of Solar.
 */
public class BlockMoltenGold extends BlockBase {

	public BlockMoltenGold() {
		super(LibNames.MOLTEN_GOLD, Material.ROCK);
		setLightLevel(1F);
		setHardness(100F);
	}

	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if(!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entityIn)) {
			entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1F);
		}
		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		BlockPos blockpos = pos.up();
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		if(iblockstate.getBlock() == Blocks.WATER || iblockstate.getBlock() == Blocks.FLOWING_WATER) {
			worldIn.setBlockToAir(blockpos);
			worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
			if(worldIn instanceof WorldServer) {
				((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.25D, (double) blockpos.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
			}
		}
		if(worldIn.getGameRules().getBoolean("doFireTick")) {
			int i = rand.nextInt(3);
			if(i > 0) {
				BlockPos pos1 = pos;

				for(int j = 0; j < i; ++j) {
					pos1 = pos1.add(rand.nextInt(3) - 1, 1, rand.nextInt(3) - 1);

					if(pos1.getY() >= 0 && pos1.getY() < worldIn.getHeight() && !worldIn.isBlockLoaded(pos1)) {
						return;
					}

					IBlockState block = worldIn.getBlockState(pos1);

					if(block.getBlock().isAir(block, worldIn, pos1)) {
						if(this.isSurroundingBlockFlammable(worldIn, pos1)) {
							worldIn.setBlockState(pos1, Blocks.FIRE.getDefaultState());
							return;
						}
					} else if(block.getMaterial().blocksMovement()) {
						return;
					}
				}
			} else {
				for(int k = 0; k < 3; ++k) {
					BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);

					if(blockpos1.getY() >= 0 && blockpos1.getY() < 256 && !worldIn.isBlockLoaded(blockpos1)) {
						return;
					}

					if(worldIn.isAirBlock(blockpos1.up()) && this.getCanBlockBurn(worldIn, blockpos1)) {
						worldIn.setBlockState(blockpos1.up(), Blocks.FIRE.getDefaultState());
					}
				}
			}
		}
	}

	private boolean isSurroundingBlockFlammable(World worldIn, BlockPos pos) {
		for(EnumFacing enumfacing : EnumFacing.values()) {
			if(this.getCanBlockBurn(worldIn, pos.offset(enumfacing))) {
				return true;
			}
		}
		return false;
	}

	private boolean getCanBlockBurn(World worldIn, BlockPos pos) {
		return (pos.getY() < 0 || pos.getY() >= 256 || worldIn.isBlockLoaded(pos)) && worldIn.getBlockState(pos).getMaterial().getCanBurn();
	}

	@Override
	@Nonnull
	public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public void registerModel() {
		//noinspection ConstantConditions
		ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), "normal");
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return mrl;
			}
		});
	}
}
