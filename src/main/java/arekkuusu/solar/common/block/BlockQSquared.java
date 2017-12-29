/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.client.render.baked.BakedPerspective;
import arekkuusu.solar.client.render.baked.BakedRender;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileQSquared;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 17/09/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQSquared extends BlockBase {

	public BlockQSquared() {
		super(LibNames.Q_SQUARED, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.DIAMOND);
		setLightLevel(1F);
		setHardness(2F);
		setSound(SoundType.GLASS);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileQSquared();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(Item.getItemFromBlock(this), (format, g) -> new BakedRender()
				.setTransforms(BakedPerspective.BLOCK_TRANSFORMS)
				.setParticle(ResourceLibrary.Q_SQUARED));
		ModelHandler.registerModel(this, 0, "");
	}
}
