/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.client.effect.ParticleUtil;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockLightParticle extends BlockBase {

	private final AxisAlignedBB box = new AxisAlignedBB(0.3D,0.3D,0.3D, 0.7D, 0.7D, 0.7D);

	public BlockLightParticle() {
		super(LibNames.LIGHT_PARTICLE, Material.GLASS);
		setLightLevel(1F);
		setHardness(2F);
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
		ParticleUtil.spawnLightParticle(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0, rand.nextDouble() * 0.025D, 0, 0xFFFFFF, 60, 5F);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return box;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
}
