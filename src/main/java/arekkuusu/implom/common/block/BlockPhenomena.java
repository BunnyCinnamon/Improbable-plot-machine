/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.common.block.tile.TilePhenomena;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/*
 * Created by <Arekkuusu> on 08/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockPhenomena extends BlockBase {

	private static final AxisAlignedBB EMPTY_BB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public BlockPhenomena() {
		super(LibNames.PHENOMENA, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(Properties.ACTIVE, true));
		setHarvestLevel(Tool.PICK, ToolLevel.DIAMOND);
		setHardness(4F);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this && block.canProvidePower(state)) {
			getTile(TilePhenomena.class, world, pos).ifPresent(phenomena -> {
				boolean wasPowered = phenomena.isPowered();
				boolean powered = world.isBlockPowered(pos);
				if(!phenomena.hasCooldown() && powered != wasPowered || !powered) {
					phenomena.setPowered(powered);
					if(powered) {
						phenomena.makePhenomenon();
					}
				}
			});
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		boolean power = state.getValue(Properties.ACTIVE);
		return !power ? EMPTY_BB : FULL_BLOCK_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean power = state.getValue(Properties.ACTIVE);
		return !power ? NULL_AABB : FULL_BLOCK_AABB;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(Properties.ACTIVE) ? 0 : 1;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(Properties.ACTIVE, meta == 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this)
				.add(Properties.ACTIVE)
				.add(net.minecraftforge.common.property.Properties.AnimationProperty)
				.build();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(Properties.ACTIVE) ? 3 : 0;
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePhenomena();
	}
}
