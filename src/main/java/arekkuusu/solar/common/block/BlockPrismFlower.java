/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.common.block.tile.TilePrismFlower;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 14/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockPrismFlower extends BlockBase implements ITileEntityProvider {

	private final AxisAlignedBB up = new AxisAlignedBB(0.3, 0, 0.3, 0.7, 1, 0.7);
	private final AxisAlignedBB down = new AxisAlignedBB(0.3, 0, 0.3, 0.7, 1, 0.7);
	private final AxisAlignedBB north = new AxisAlignedBB(0.3, 0.45, 0, 0.7, 0.9, 0.8);
	private final AxisAlignedBB south = new AxisAlignedBB(0.3, 0.45, 0.2, 0.7, 0.9, 1);
	private final AxisAlignedBB east = new AxisAlignedBB(0.2, 0.45, 0.3, 1, 0.9, 0.7);
	private final AxisAlignedBB west = new AxisAlignedBB(0, 0.45, 0.3, 0.8, 0.9, 0.7);

	public BlockPrismFlower() {
		super(LibNames.PRISM_FLOWER, Material.PLANTS);
		setDefaultState(defaultState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
		setLightLevel(0.1F);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		BlockPos offset = pos.offset(facing);
		if(!world.getBlockState(offset).isSideSolid(world, offset, facing)) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		IBlockState state = worldIn.getBlockState(pos.offset(side.getOpposite()));
		return state.getBlock().isSideSolid(state, worldIn, pos, side);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite());
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockDirectional.FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return defaultState().withProperty(BlockDirectional.FACING, EnumFacing.values()[meta]);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING);
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
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		switch(facing) {
			case UP:
				return up;
			case NORTH:
				return north;
			case SOUTH:
				return south;
			case WEST:
				return west;
			case EAST:
				return east;
			default:
				return down;
		}
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TilePrismFlower();
	}
}
