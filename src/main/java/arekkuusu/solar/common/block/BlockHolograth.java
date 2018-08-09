/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import static net.minecraft.block.BlockHorizontal.FACING;

/**
 * Created by <Arekkuusu> on 8/8/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockHolograth extends BlockBaseHorizontal {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

	public BlockHolograth() {
		super(LibNames.HOLOGRATH, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
		setHarvestLevel(Tool.PICK, ToolLevel.IRON);
		setHardness(2F);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
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
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.SOLID;
	}
}
