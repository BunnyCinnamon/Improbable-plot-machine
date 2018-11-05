package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.tile.TileCrystallicSynthesizer;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class BlockCrystallicSynthesizer extends BlockBase {

	public BlockCrystallicSynthesizer() {
		super(LibNames.CRYSTALLIC_SYNTHESIZER, FixedMaterial.DONT_MOVE);
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
		return new TileCrystallicSynthesizer();
	}

	@Override
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered()
				.setParticle(ResourceLibrary.CRYSTALLIC)
		);
		ModelHandler.registerModel(this, 0);
	}
}
