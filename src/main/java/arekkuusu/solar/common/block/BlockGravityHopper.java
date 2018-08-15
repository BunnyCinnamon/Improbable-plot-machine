/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.common.block.tile.TileGravityHopper;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
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

import static net.minecraft.block.BlockDirectional.FACING;

/*
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockGravityHopper extends BlockBaseFacing {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.3D,0.3D,0.3D, 0.7D, 0.7D, 0.7D);

	public BlockGravityHopper() {
		super(LibNames.GRAVITY_HOPPER, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.DOWN));
		setHarvestLevel(Tool.PICK, ToolLevel.IRON);
		setHardness(2F);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {
			getTile(TileGravityHopper.class, world, pos).ifPresent(hopper -> {
				boolean wasPowered = hopper.isPowered();
				boolean isPowered = world.isBlockPowered(pos);
				if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
					hopper.setPowered(isPowered);
					if(isPowered != hopper.isInverse()) {
						hopper.setInverse(isPowered);
					}
				}
			});
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing side = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		return defaultState().withProperty(FACING, side);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.SOLID;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileGravityHopper();
	}
}
