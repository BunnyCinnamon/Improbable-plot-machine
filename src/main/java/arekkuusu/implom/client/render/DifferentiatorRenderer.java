/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.GLHelper;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileDifferentiator;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.MinecraftForgeClient;

/*
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class DifferentiatorRenderer extends SpecialModelRenderer<TileDifferentiator> {

	@Override
	void renderTile(TileDifferentiator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		switch(te.getFacingLazy()) {
			case DOWN:
				GlStateManager.rotate(180F, 1F, 0F, 0F);
				break;
			case NORTH:
				GlStateManager.rotate(90F, -1F, 0F, 0F);
				break;
			case SOUTH:
				GlStateManager.rotate(90F, 1F, 0F, 0F);
				break;
			case WEST:
				GlStateManager.rotate(90F, 0F, 0F, 1F);
				break;
			case EAST:
				GlStateManager.rotate(90F, 0F, 0F, -1F);
				break;
			case UP: break;
		}
		switch(MinecraftForgeClient.getRenderPass()) {
			case 0:
				BlockBaker.DIFFERENTIATOR_BASE.render();
				//Rings
				float tick = RenderHelper.getRenderWorldTime(partialTicks);
				GlStateManager.pushMatrix(); //Bottom
				GlStateManager.rotate(partialTicks + tick * 0.15F % 360F, 0F, 1F, 0F);
				BlockBaker.DIFFERENTIATOR_RING_BOTTOM.render();
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix(); //Middle
				GlStateManager.rotate(partialTicks + tick * 0.25F % 360F, 0F, 1F, 0F);
				BlockBaker.DIFFERENTIATOR_RING_MIDDLE.render();
				GlStateManager.popMatrix();
				GlStateManager.pushMatrix(); //Top
				GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
				BlockBaker.DIFFERENTIATOR_RING_TOP.render();
				GlStateManager.popMatrix();
				break;
			case 1:
				//Inner core
				GlStateManager.enableBlend();
				GlStateManager.disableCull();
				GlStateManager.disableLighting();
				ShaderLibrary.BRIGHT.begin();
				ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
					b.set(-0.2F);
					b.upload();
				});
				BlockBaker.DIFFERENTIATOR_CORE.render();
				ShaderLibrary.BRIGHT.end();
				GlStateManager.enableLighting();
				GlStateManager.enableCull();
				GlStateManager.disableBlend();
				break;
		}
		GlStateManager.popMatrix();
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		BlockBaker.DIFFERENTIATOR_BASE.render();
		//Rings
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix(); //Bottom
		GlStateManager.rotate(partialTicks + tick * 0.05F % 360F, 0F, 1F, 0F);
		BlockBaker.DIFFERENTIATOR_RING_BOTTOM.render();
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix(); //Middle
		GlStateManager.rotate(partialTicks + tick * 0.15F % 360F, 0F, 1F, 0F);
		BlockBaker.DIFFERENTIATOR_RING_MIDDLE.render();
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix(); //Top
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		BlockBaker.DIFFERENTIATOR_RING_TOP.render();
		GlStateManager.popMatrix();
		//Inner core
		GLHelper.disableDepth();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		Blending.AdditiveAlpha().apply();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.DIFFERENTIATOR_CORE.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GLHelper.enableDepth();
		GlStateManager.popMatrix();
	}
}
