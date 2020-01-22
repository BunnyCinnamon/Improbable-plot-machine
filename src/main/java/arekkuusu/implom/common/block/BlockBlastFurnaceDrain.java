package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.block.base.BlockBaseFacing;
import arekkuusu.implom.common.block.base.BlockBaseMultiblock;
import arekkuusu.implom.common.block.tile.TileBlastFurnaceDrain;
import arekkuusu.implom.common.block.tile.TileMultiblockImouto;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBlastFurnaceDrain extends BlockBaseFacing {

	public BlockBlastFurnaceDrain() {
		super(LibNames.BLAST_FURNACE_DRAIN, IPMMaterial.FIRE_BRICK);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.WOOD_GOLD);
		setHardness(1F);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		BlockBaseMultiblock.handleBreakBlock(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		BlockBaseMultiblock.handleOnBlockPlacedBy(worldIn, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileBlastFurnaceDrain();
	}
}
