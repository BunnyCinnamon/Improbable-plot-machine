/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.BakerLibrary;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileFissionInducer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/5/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileFissionInducerRenderer extends net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer<TileFissionInducer> {

	@Override
	public void render(TileFissionInducer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		//Center
		BakerLibrary.FISSION_INDUCER_FRAME.renderWithRotation(facing);
		//Top & Bottom
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick, 0.015F, 1.5F);
		BakerLibrary.FISSION_INDUCER_TOP.renderWithRotation(facing);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(-tick, 0.015F, 1.5F);
		BakerLibrary.FISSION_INDUCER_BOTTOM.renderWithRotation(facing);
		GlStateManager.popMatrix();
		//Inside
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			float brigthness = MathHelper.cos(Minecraft.getMinecraft().player.ticksExisted * 0.05F);
			brigthness *= 0.1F;
			b.set(brigthness);
			b.upload();
		});
		BakerLibrary.FISSION_INDUCER_CORE.renderWithRotation(facing);
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
