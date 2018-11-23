/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render;

import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileDifferentiatorInterceptor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;

/*
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class DifferentiatorInterceptorRenderer extends SpecialModelRenderer<TileDifferentiatorInterceptor> {

	@Override
	void renderTile(TileDifferentiatorInterceptor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		switch (te.getFacingLazy()) {
			case UP:
				GlStateManager.rotate(180, 1F, 0F, 0F);
				break;
			case NORTH:
				GlStateManager.rotate(90F, 1F, 0F, 0F);
				break;
			case SOUTH:
				GlStateManager.rotate(90F, -1F, 0F, 0F);
				break;
			case WEST:
				GlStateManager.rotate(90F, 0F, 0F, -1F);
				break;
			case EAST:
				GlStateManager.rotate(90F, 0F, 0F, 1F);
				break;
			case DOWN:
				break;
		}
		renderModel(partialTicks);
		GlStateManager.popMatrix();
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		renderModel(partialTicks);
		GlStateManager.popMatrix();
	}

	private void renderModel(float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		BlockBaker.DIFFERENTIATOR_INTERCEPTOR_BASE.render();
		//Ring
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.15F % 360F, 0F, 1F, 0F);
		BlockBaker.DIFFERENTIATOR_RING.render();
		GlStateManager.popMatrix();
	}
}
