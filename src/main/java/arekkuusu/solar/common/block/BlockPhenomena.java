/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.common.block.tile.TilePhenomena;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

/**
 * Created by <Arekkuusu> on 08/09/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockPhenomena extends BlockBase {

	private static final AxisAlignedBB EMPTY_BB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public BlockPhenomena() {
		super(LibNames.PHENOMENA, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(State.ACTIVE, true));
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
		boolean power = state.getValue(State.ACTIVE);
		return !power ? EMPTY_BB : FULL_BLOCK_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean power = state.getValue(State.ACTIVE);
		return !power ? NULL_AABB : FULL_BLOCK_AABB;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(State.ACTIVE) ? 0 : 1;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(State.ACTIVE, meta == 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[]{State.ACTIVE}, new IUnlistedProperty[]{Properties.AnimationProperty});
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(State.ACTIVE) ? 3 : 0;
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
