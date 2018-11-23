/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.lib.LibNames;
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
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockImbuedQuartz extends BlockBase {

	public BlockImbuedQuartz() {
		super(LibNames.IMBUED_QUARTZ, Material.GLASS);
		setSound(SoundType.GLASS);
		setHardness(0.3F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		for(int i = 0; i < 1 + rand.nextInt(4); i++) {
			Vector3 posVec = new Vector3.WrappedVec3i(pos).asImmutable().add(Math.random(), Math.random(), Math.random());
			IPM.getProxy().spawnSpeck(world, posVec, Vector3.rotateRandom().multiply(0.01D), 45, rand.nextFloat(), 0x49FFFF, GlowTexture.GLOW);
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		if(iblockstate.getBlock() == ModBlocks.IMBUED_QUARTZ) return false;
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		IBlockState iblockstate = world.getBlockState(pos.offset(face));
		if(iblockstate.getBlock() == ModBlocks.IMBUED_QUARTZ) return false;
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
