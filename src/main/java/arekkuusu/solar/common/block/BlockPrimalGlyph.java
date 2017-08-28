/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.Glyph;
import arekkuusu.solar.client.render.baked.PrimalGlyphBakedModel;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static arekkuusu.solar.api.state.Glyph.GLYPH;

/**
 * Created by <Arekkuusu> on 24/06/2017.
 * It's distributed as part of Solar.
 */
public class BlockPrimalGlyph extends BlockBase {

	public BlockPrimalGlyph() {
		super(LibNames.PRIMAL_GLYPH, Material.ROCK);
		setDefaultState(defaultState().withProperty(GLYPH, Glyph.A));
		setHarvestLevel("pickaxe", 1);
		setHardness(4F);
		setResistance(2000F);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(GLYPH, Glyph.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(GLYPH).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, GLYPH);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for(int i = 0; i < 16; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(Item.getItemFromBlock(this), pair -> new PrimalGlyphBakedModel(pair.getLeft(), pair.getRight()));
		ModelHandler.registerModel(this, 0, "");
	}
}
