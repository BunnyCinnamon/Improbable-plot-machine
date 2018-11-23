package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.tile.TileCrystalSynthesizer;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockCrystalSynthesizer extends BlockBase {

	public BlockCrystalSynthesizer() {
		super(LibNames.CRYSTAL_SYNTHESIZER, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.IRON);
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

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileCrystalSynthesizer();
	}

	@Override
	public void registerModel() {
		ModelHandler.registerModel(this, 0);
	}
}
