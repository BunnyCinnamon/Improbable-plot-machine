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
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		renderModel(te.getFacingLazy(), tick, x, y, z, partialTicks);
	}

	public static void renderModel(@Nullable EnumFacing facing, float tick, double x, double y, double z, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		if(facing != null && facing != EnumFacing.DOWN) {
			switch(facing) {
				case UP:
					GlStateManager.rotate(180F, 1F, 0F, 0F);
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
			}
		}
		//Center
		BlockBaker.FISSION_INDUCER_CENTER.render();
		//Top & Bottom
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick, 0.015F, 1.5F, partialTicks);
		BlockBaker.FISSION_INDUCER_TOP.render();
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(-tick, 0.015F, 1.5F, partialTicks);
		BlockBaker.FISSION_INDUCER_BOTTOM.render();
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
		BlockBaker.FISSION_INDUCER_INSIDE.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
