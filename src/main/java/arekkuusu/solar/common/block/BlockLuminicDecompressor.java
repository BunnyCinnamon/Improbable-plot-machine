/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.common.block.tile.TileLuminicDecompressor;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockDirectional;
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

import javax.annotation.Nullable;

/*
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockLuminicDecompressor extends BlockBaseFacing {

	public static final int REACH = 5;
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0, 0.5625, 0, 1, 1, 1))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0, 0, 0, 1, 0.4375, 1))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0, 0, 0, 1, 1, 0.4375))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0, 0, 0.5625, 1, 1, 1))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.5625, 0, 0, 1, 1, 1))
			.put(EnumFacing.WEST, new AxisAlignedBB(0, 0, 0, 0.4375, 1, 1))
			.build();

	public BlockLuminicDecompressor() {
		super(LibNames.LUMINIC_DECOMPRESSOR, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
		setLightLevel(0.2F);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileLuminicDecompressor();
	}
}
