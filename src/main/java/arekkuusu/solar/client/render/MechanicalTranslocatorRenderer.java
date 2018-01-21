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
import arekkuusu.solar.common.block.tile.TileMechanicalTranslocator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created by <Snack> on 21/01/2018.
 * It's distributed as part of Solar.
 */
public class MechanicalTranslocatorRenderer extends SpecialModelRenderer<TileMechanicalTranslocator> {

	@Override
	void renderTile(TileMechanicalTranslocator translocator, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(translocator.getFacingLazy(), x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(null, x, y, z, partialTicks);
	}

	private void renderModel(@Nullable EnumFacing facing, double x, double y, double z, float partialTicks) {
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
		float tick = Minecraft.getSystemTime();
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
					GlStateManager.rotate(90F, 0F, 0F, 1F);
					break;
				case EAST:
					GlStateManager.rotate(90F, -1F, 0F, 0F);
					break;
			}
		}
		//Top & Bottom
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		BlockBaker.render(BlockBaker.TRANSLOCATOR_BASE);
		GlStateManager.popMatrix();
		//Middle
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, -1F, 0F);
		GlStateManager.disableLighting();
		GLHelper.lightMap(255F, 255F);
		BlockBaker.render(BlockBaker.TRANSLOCATOR_CENTER);
		GLHelper.lightMap(prevU, prevV);
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		//Piece
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0.75F, 0F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 360F, 0F, 1F, 0F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 270F, 1F, 0F, 0F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 180F, 0F, 0F, 1F);
		BlockBaker.render(BlockBaker.TRANSLOCATOR_PIECE_RING);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
