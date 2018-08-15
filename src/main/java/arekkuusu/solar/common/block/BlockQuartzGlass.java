/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/*
 * Created by <Arekkuusu> on 8/12/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQuartzGlass extends BlockBase {

	public BlockQuartzGlass() {
		super(LibNames.QUARTZ_GLASS, Material.GLASS);
		setSound(SoundType.GLASS);
		setHardness(0.3F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		for(int i = 0; i < 1 + rand.nextInt(4); i++) {
			Vector3 posVec = new Vector3.WrappedVec3i(pos).asImmutable().add(Math.random(), Math.random(), Math.random());
			Solar.PROXY.spawnSpeck(world, posVec, Vector3.rotateRandom().multiply(0.01D), 45, rand.nextFloat(), (int) (Math.random() * 0x1000000), GlowTexture.GLOW);
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		if(iblockstate.getBlock() == ModBlocks.QUARTZ_GLASS) return false;
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		IBlockState iblockstate = world.getBlockState(pos.offset(face));
		if(iblockstate.getBlock() == ModBlocks.QUARTZ_GLASS) return false;
		return super.doesSideBlockRendering(state, world, pos, face);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
}
