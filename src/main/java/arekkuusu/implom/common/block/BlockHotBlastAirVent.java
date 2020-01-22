package arekkuusu.implom.common.block;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.block.base.BlockBase;
import arekkuusu.implom.common.block.tile.TileHotBlastAirVent;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockHotBlastAirVent extends BlockBase {

	public BlockHotBlastAirVent() {
		super(LibNames.BLAST_FURNACE_AIR_VENT, IPMMaterial.FIRE_BRICK);
		setDefaultState(getDefaultState().withProperty(Properties.ACTIVE, false));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(blockIn != this) {
			getTile(TileHotBlastAirVent.class, worldIn, pos).ifPresent(tile -> {
				boolean isPowered = worldIn.isBlockPowered(pos);
				if(isPowered) {
					if(!tile.isStructureActive) {
						tile.okaeriOniichan();
					}
					else {
						tile.setInvalid();
					}
				}
			});
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(Properties.ACTIVE, meta == 1);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(Properties.ACTIVE) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, Properties.ACTIVE);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return !state.getValue(Properties.ACTIVE);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileHotBlastAirVent();
	}
}
