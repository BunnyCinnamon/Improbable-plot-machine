/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileTheorema;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.client.baked.BakedRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 22/09/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockTheorema extends BlockBase {

	public BlockTheorema() {
		super(LibNames.THEOREMA, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.DIAMOND);
		setHardness(5F);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileTheorema();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, BakedRender::new);
		ModelHandler.registerModel(this, 0, "");
	}
}
