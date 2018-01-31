/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.ModBlocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
public class ItemMonolithicGlyph extends ItemBaseBlock {

	public ItemMonolithicGlyph() {
		super(ModBlocks.MONOLITHIC_GLYPH);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		for(int i = 0; i < 16; i++) {
			ModelHandler.registerModel(this, i, ResourceLibrary.getModel("monolithic_glyph_", "glyph=" + i));
		}
	}
}
