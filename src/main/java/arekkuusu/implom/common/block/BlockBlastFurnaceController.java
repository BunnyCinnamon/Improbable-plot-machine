package arekkuusu.implom.common.block;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.block.base.BlockBaseHorizontal;
import arekkuusu.implom.common.block.tile.TileBlastFurnaceController;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBlastFurnaceController extends BlockBaseHorizontal {

	public BlockBlastFurnaceController() {
		super(LibNames.BLAST_FURNACE_CONTROLLER, IPMMaterial.FIRE_BRICK);
		setDefaultState(getDefaultState().withProperty(Properties.ACTIVE, false).withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
		setHarvestLevel(Tool.PICK, ToolLevel.WOOD_GOLD);
		setHardness(1F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			getTile(TileBlastFurnaceController.class, worldIn, pos).ifPresent(tile -> {
				if(playerIn.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL)
					tile.okaeriOniichan();
				else if(playerIn.isSneaking())
					tile.setInvalid();
			});
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta & 7);
		if(enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(BlockHorizontal.FACING, enumfacing).withProperty(Properties.ACTIVE, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(BlockHorizontal.FACING).getIndex();
		if(state.getValue(Properties.ACTIVE)) {
			i |= 8;
		}
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockHorizontal.FACING, Properties.ACTIVE);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(Properties.ACTIVE) ? 5 : 0;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileBlastFurnaceController();
	}
}
