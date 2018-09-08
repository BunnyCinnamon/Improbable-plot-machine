package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileCrystallicSynthesizer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.math.MathHelper;

public class CrystallicSynthesizerRenderer extends SpecialModelRenderer<TileCrystallicSynthesizer> {

	@Override
	void renderTile(TileCrystallicSynthesizer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(x, y, z, partialTicks);
	}

	private void renderModel(double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		RenderHelper.makeUpDownTranslation(RenderHelper.getRenderWorldTime(partialTicks), 0.010F, 0.15F, 0F);
		BlockBaker.CRYSTALLIC_SYNTHESIZER_BASE.render();
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		RenderHelper.makeUpDownTranslation(RenderHelper.getRenderWorldTime(partialTicks), 0.005F, 1F, 15F);
		ShaderLibrary.RECOLOR.begin();
		ShaderLibrary.RECOLOR.getUniformJ("greybase").ifPresent(greybase -> {
			greybase.set(175F / 256F);
			greybase.upload();
		});
		ShaderLibrary.RECOLOR.getUniformJ("rgba").ifPresent(rgba -> {
			rgba.set(0.15F, 0.15F, 0.15F);
			rgba.upload();
		});
		BlockBaker.CRYSTALLIC_SYNTHESIZER_CRYSTAL.render();
		ShaderLibrary.RECOLOR.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(RenderHelper.getRenderWorldTime(partialTicks), 0.010F, 0.5F, 15F);
		BlockBaker.CRYSTALLIC_SYNTHESIZER_RING.render();
		GlStateManager.disableLighting();
		ShaderLibrary.RECOLOR.begin();
		ShaderLibrary.RECOLOR.getUniformJ("greybase").ifPresent(greybase -> {
			greybase.set(175F / 256F);
			greybase.upload();
		});
		ShaderLibrary.RECOLOR.getUniformJ("rgba").ifPresent(rgba -> {
			float brigthness = MathHelper.sin(RenderHelper.getRenderPlayerTime() * 0.25F);
			if(brigthness < 0) brigthness *= -1;
			brigthness *= 0.15F;
			rgba.set(1F - brigthness, 0.72F - brigthness, 0F);
			rgba.upload();
		});
		BlockBaker.CRYSTALLIC_SYNTHESIZER_RING_GLOW.render();
		ShaderLibrary.RECOLOR.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		BlockBaker.CRYSTALLIC_SYNTHESIZER_NODE.render();
		GlStateManager.disableLighting();
		ShaderLibrary.RECOLOR.begin();
		ShaderLibrary.RECOLOR.getUniformJ("greybase").ifPresent(greybase -> {
			greybase.set(175F / 256F);
			greybase.upload();
		});
		ShaderLibrary.RECOLOR.getUniformJ("rgba").ifPresent(rgba -> {
			rgba.set(0.15F, 0.15F, 0.15F);
			rgba.upload();
		});
		BlockBaker.CRYSTALLIC_SYNTHESIZER_NODE_GLOW.render();
		ShaderLibrary.RECOLOR.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
