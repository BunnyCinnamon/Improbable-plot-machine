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

public class BlockBaseMultiblock extends BlockBase {

	public static void handleBreakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IMultiblockImouto) {
			((IMultiblockImouto) tile).wakeUpOniichan();
		}
		world.removeTileEntity(pos);
	}

	public static void handleOnBlockPlacedBy(World worldIn, BlockPos pos) {
		for(EnumFacing dir : EnumFacing.values()) {
			TileEntity tile = worldIn.getTileEntity(pos.offset(dir));
			if(tile instanceof IMultiblockOniichan) {
				((IMultiblockOniichan) tile).okaeriOniichan();
			}
			else if(tile instanceof IMultiblockImouto) {
				IMultiblockImouto imouto = (IMultiblockImouto) tile;
				if(imouto.hasValidOniichan()) {
					imouto.wakeUpOniichan();
				}
			}
		}
	}

	public BlockBaseMultiblock(String id, Material material) {
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
