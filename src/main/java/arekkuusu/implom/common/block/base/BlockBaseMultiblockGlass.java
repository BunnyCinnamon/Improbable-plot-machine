package arekkuusu.implom.common.block.base;

import arekkuusu.implom.api.multiblock.IMultiblockImouto;
import arekkuusu.implom.api.multiblock.IMultiblockOniichan;
import arekkuusu.implom.common.block.tile.TileMultiblockImouto;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBaseMultiblockGlass extends BlockBaseGlass {

	public BlockBaseMultiblockGlass(String id, Material material) {
		super(id, material);
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
		return new TileMultiblockImouto();
	}
}
