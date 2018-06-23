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
import arekkuusu.solar.client.util.helper.GLHelper;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileDifferentiatorInterceptor;
import net.katsstuff.mirror.client.helper.Blending;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Solar.
 */
public class DifferentiatorInterceptorRenderer extends SpecialModelRenderer<TileDifferentiatorInterceptor> {

	@Override
	void renderTile(TileDifferentiatorInterceptor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		switch(te.getFacingLazy()) {
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
			case DOWN: break;
		}
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		RenderHelper.makeUpDownTranslation(tick, 0.01F, 0.5F, 0F);
		switch(MinecraftForgeClient.getRenderPass()) {
			case 0:
				BlockBaker.DIFFERENTIATOR_INTERCEPTOR_BASE.render();
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.disableCull();
				ShaderLibrary.BRIGHT.begin();
				ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
					b.set(-0.2F);
					b.upload();
				});
				BlockBaker.DIFFERENTIATOR_INTERCEPTOR_GLASS.render();
				ShaderLibrary.BRIGHT.end();
				GlStateManager.enableCull();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				//Ring
				GlStateManager.pushMatrix();
				GlStateManager.rotate(partialTicks + tick * 0.15F % 360F, 0F, 1F, 0F);
				BlockBaker.DIFFERENTIATOR_INTERCEPTOR_RING.render();
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix();
				GlStateManager.rotate(partialTicks + tick * 0.25F % 360F, 0F, 1F, 0F);
				BlockBaker.DIFFERENTIATOR_INTERCEPTOR_RING_.render();
				GlStateManager.popMatrix();
				break;
			case 1:
				//Inner core
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.disableCull();
				ShaderLibrary.BRIGHT.begin();
				ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
					b.set(-0.2F);
					b.upload();
				});
				BlockBaker.DIFFERENTIATOR_INTERCEPTOR_BEACON.render();
				ShaderLibrary.BRIGHT.end();
				GlStateManager.enableCull();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				break;
		}
		GlStateManager.popMatrix();
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		RenderHelper.makeUpDownTranslation(tick, 0.01F, 0.5F, 0F);
		BlockBaker.DIFFERENTIATOR_INTERCEPTOR_BASE.render();
		//Ring
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.15F % 360F, 0F, 1F, 0F);
		BlockBaker.DIFFERENTIATOR_RING_BOTTOM.render();
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.25F % 360F, 0F, 1F, 0F);
		BlockBaker.DIFFERENTIATOR_INTERCEPTOR_RING_.render();
		GlStateManager.popMatrix();
		//Inner core
		GlStateManager.disableLighting();
		Blending.AdditiveAlpha().apply();
		BlockBaker.DIFFERENTIATOR_INTERCEPTOR_GLASS.render();
		GLHelper.disableDepth();
		GlStateManager.disableCull();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.DIFFERENTIATOR_INTERCEPTOR_BEACON.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableCull();
		GLHelper.enableDepth();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
