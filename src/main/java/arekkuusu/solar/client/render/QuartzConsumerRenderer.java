/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileQuartzConsumer;
import arekkuusu.solar.common.item.ModItems;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

/**
 * Created by <Arekkuusu> on 4/30/2018.
 * It's distributed as part of Solar.
 */
public class QuartzConsumerRenderer extends TileEntitySpecialRenderer<TileQuartzConsumer> {

	private final ItemStack stack = new ItemStack(ModItems.CRYSTAL_QUARTZ);;

	@Override
	public void render(TileQuartzConsumer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(te.getHasItem()) {
			float tick = RenderHelper.getRenderWorldTime(partialTicks);
			ShaderLibrary.BRIGHT.begin();
			ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
				b.set(0F);
				b.upload();
			});
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.48, z + 0.5);
			GlStateManager.scale(0.65D, 0.65D,0.65D);
			GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
			RenderHelper.renderItemStack(stack);
			GlStateManager.popMatrix();
			ShaderLibrary.BRIGHT.end();
		}
	}
}
