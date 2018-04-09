/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileFissionInducer;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.client.baked.BakedRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/4/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockFissionInducer extends BlockBaseFacing {

	public BlockFissionInducer() {
		super(LibNames.FISSION_INDUCER, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(5F);
		setResistance(2000F);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFissionInducer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.FISSION_INDUCER)
		);
		ModelHandler.registerModel(this, 0);
	}
}
