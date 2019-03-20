/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileElectron;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;

/*
 * Created by <Arekkuusu> on 4/11/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileElectronRenderer extends net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer<TileElectron> {

	@Override
	public void render(TileElectron te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		renderModel(te.power > 0, tick, x, y, z, partialTicks);
	}

	public static void renderModel(boolean active, float tick, double x, double y, double z, float partialTicks) {
		if(active) {
			GlStateManager.disableLighting();
			ShaderLibrary.BRIGHT.begin();
			ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
				b.set(0F);
				b.upload();
			});
			tick *= 0.25F; //haha yes
		} else tick *= -0.25F; //haha no
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
