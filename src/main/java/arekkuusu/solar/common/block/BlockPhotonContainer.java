/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.common.entity.Megumin;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockPhotonContainer extends BlockBase {

	public BlockPhotonContainer() {
		super(LibNames.PHOTON_CONTAINER, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(State.ACTIVE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(0.3F);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if(state.getValue(State.ACTIVE)) {
			Vector3 vec = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			Megumin.chant(world, vec, 5F, true).explosion();
			if(!world.isRemote) world.setBlockToAir(pos);
		}
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(State.ACTIVE) ? 5 : 0;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(State.ACTIVE) ? 0 : 1;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(State.ACTIVE, meta == 1);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, State.ACTIVE);
	}
}
