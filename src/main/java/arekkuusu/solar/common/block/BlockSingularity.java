/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.material.FixedMaterial;
import arekkuusu.solar.common.block.tile.TileSingularity;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockSingularity extends BlockBase implements ITileEntityProvider {

	private final AxisAlignedBB box = new AxisAlignedBB(0.3D,0.3D,0.3D, 0.7D, 0.7D, 0.7D);

	public BlockSingularity() {
		super(LibNames.SINGULARITY, FixedMaterial.DONT_MOVE);
		setHarvestLevel("pickaxe", 3);
		setLightLevel(1F);
		setHardness(10F);
		setResistance(2000F);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return box;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileSingularity();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof TileSingularity) {
			((TileSingularity) tile).removeAll();
		}
		worldIn.removeTileEntity(pos);
	}
}
