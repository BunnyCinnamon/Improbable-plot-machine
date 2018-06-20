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
import arekkuusu.solar.common.block.tile.TileLumenCompressor;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 5/2/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockLumenCompressor extends BlockBaseFacing {

	public BlockLumenCompressor() {
		super(LibNames.LUMEN_COMPRESSOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		/*BlockPos fromPos = pos.offset(state.getValue(BlockDirectional.FACING).getOpposite());
		IBlockState fromState = world.getBlockState(fromPos);
		if(fromState.getBlock() == ModBlocks.PHOTON_CONTAINER && fromState.getValue(State.ACTIVE)) {
			Vector3 vec = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			world.createExplosion(null, vec.getX(), vec.getY(), vec.getZ(), 5F, true);
			if(!world.isRemote) world.setBlockToAir(fromPos);
		}*/
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		/*if(block != this && pos.offset(state.getValue(BlockDirectional.FACING).getOpposite()).equals(fromPos)) {
			getTile(TileLumenCompressor.class, world, pos).filter(TileLumenCompressor::hasLumen).ifPresent(t -> {
				IBlockState fromState = world.getBlockState(fromPos);
				if(fromState.getBlock() == ModBlocks.PHOTON_CONTAINER) {
					world.setBlockState(fromPos, fromState.withProperty(State.ACTIVE, true));
				}
			});
		}*/
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
		return state.getValue(BlockDirectional.FACING).getOpposite() == facing ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.SOLID;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileLumenCompressor();
	}
}
