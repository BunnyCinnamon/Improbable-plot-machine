/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.material.FixedMaterial;
import arekkuusu.solar.common.block.tile.TileHyperConductor;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static arekkuusu.solar.api.state.Power.POWER_AMOUNT;

/**
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockHyperConductor extends BlockBase {

	public BlockHyperConductor() {
		super(LibNames.HYPER_CONDUCTOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(POWER_AMOUNT, 0));
		setHardness(1F);
		setTickRandomly(true);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {
			getTile(TileHyperConductor.class, world, pos).ifPresent(conductor -> {
				boolean wasPowered = conductor.isPowered();
				boolean isPowered = world.isBlockPowered(pos);
				if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
					conductor.setPowered(isPowered);
				}
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileHyperConductor.class, world, pos).ifPresent(conductor -> {
			if(state.getValue(POWER_AMOUNT) > 0) {
				conductor.setPowered(false);
			}
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(POWER_AMOUNT, 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POWER_AMOUNT);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(POWER_AMOUNT, meta);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POWER_AMOUNT);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileHyperConductor();
	}
}
