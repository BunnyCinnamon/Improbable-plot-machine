/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.item;

import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.lib.LibMod;
import net.katsstuff.teamnightclipse.mirror.client.helper.ResourceHelperStatic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Improbable plot machine.
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
		for (int i = 0; i < 16; i++) {
			ModelHandler.registerModel(this, i, ResourceHelperStatic.getModel(LibMod.MOD_ID, "monolithic_glyph", "glyph=" + i));
		}
	}
}
