package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileCrystallicSynthesizer;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedRender;
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
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.CRYSTALLIC)
		);
		ModelHandler.registerModel(this, 0);
	}
}
