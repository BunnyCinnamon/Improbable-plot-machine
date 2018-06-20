/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileElectron;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;

/**
 * Created by <Arekkuusu> on 4/11/2018.
 * It's distributed as part of Solar.
 */
public class ElectronRenderer extends SpecialModelRenderer<TileElectron> {

	@Override
	void renderTile(TileElectron te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		renderModel(te.isActiveLazy(), tick, x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		renderModel(true, tick, x, y, z, partialTicks);
	}

	private void renderModel(boolean active, float tick, double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		if(active) {
			GlStateManager.disableLighting();
			ShaderLibrary.BRIGHT.begin();
			ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
				b.set(0F);
				b.upload();
			});
			tick *= 0.25F; //haha yes
		} else 	tick *= -0.25F; //haha no
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 360F, 0F, 1F, 0F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 360F, 1F, 0F, 0F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 360F, 0F, 0F, 1F);
		BlockBaker.ELECTRON.render();
		if(active) {
			GlStateManager.enableLighting();
			ShaderLibrary.BRIGHT.end();
		}
		GlStateManager.popMatrix();
	}
}
