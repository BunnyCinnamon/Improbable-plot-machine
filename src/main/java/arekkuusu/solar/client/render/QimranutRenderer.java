/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.client.util.helper.GLHelper;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileQimranut;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Snack> on 30/01/2018.
 * It's distributed as part of Solar.
 */
public class QimranutRenderer extends SpecialModelRenderer<TileQimranut> {

	@Override
	void renderTile(TileQimranut te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(null, x, y, z, partialTicks);
	}

	private void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
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
		//Base
		BlockBaker.render(BlockBaker.QIMRANUT);
		//Base Overlay
		GlStateManager.disableLighting();
		GLHelper.lightMap(255F, 255F);
		BlockBaker.render(BlockBaker.QIMRANUT_);
		//Piece
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		/*--- Sides ---*/
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		BlockBaker.render(BlockBaker.QIMRANUT_PIECE_0);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, -1F, 0F);
		BlockBaker.render(BlockBaker.QIMRANUT_PIECE_1);
		GlStateManager.popMatrix();
		/*--- Corners ---*/
		GLHelper.lightMap(prevU, prevV);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
