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
import arekkuusu.solar.common.block.tile.TileFissionInducer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/5/2018.
 * It's distributed as part of Solar.
 */
public class FissionInducerRenderer extends SpecialModelRenderer<TileFissionInducer> {

	@Override
	void renderTile(TileFissionInducer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		renderModel(te.getFacingLazy(), tick, x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		renderModel(null, tick, x, y, z, partialTicks);
	}

	private void renderModel(@Nullable EnumFacing facing, float tick, double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
		BlockBaker.render(BlockBaker.FISSION_INDUCER_CENTER);
		//Top & Bottom
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick, 0.015F, 1.5F, partialTicks);
		BlockBaker.render(BlockBaker.FISSION_INDUCER_TOP);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(-tick, 0.015F, 1.5F, partialTicks);
		BlockBaker.render(BlockBaker.FISSION_INDUCER_BOTTOM);
		GlStateManager.popMatrix();
		//Inside
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.render(BlockBaker.FISSION_INDUCER_INSIDE);
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
