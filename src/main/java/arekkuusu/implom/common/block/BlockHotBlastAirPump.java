package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.block.base.BlockBaseHorizontal;
import arekkuusu.implom.common.block.base.BlockBaseMultiblock;
import arekkuusu.implom.common.block.tile.TileHotBlastAirPump;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHotBlastAirPump extends BlockBaseHorizontal {

	public BlockHotBlastAirPump() {
		super(LibNames.BLAST_FURNACE_AIR_PUMP, IPMMaterial.FIRE_BRICK);
		setDefaultState(getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
		setHarvestLevel(Tool.PICK, ToolLevel.WOOD_GOLD);
		setHardness(1F);
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
		return new TileHotBlastAirPump();
	}
}
