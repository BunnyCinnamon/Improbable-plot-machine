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
import arekkuusu.solar.common.block.tile.TileVacuumConveyor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Snack> on 07/01/2018.
 * It's distributed as part of Solar.
 */
public class VacuumConveyorRenderer extends SpecialModelRenderer<TileVacuumConveyor> {

	@Override
	void renderTile(TileVacuumConveyor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(null, x, y, z, partialTicks);
	}

	private void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
		float tick = Minecraft.getSystemTime();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//Top
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		if(facing != null && facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
			GlStateManager.rotate(90F, facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? 1F : 0F, 0F, facing == EnumFacing.EAST || facing == EnumFacing.WEST ? 1F : 0F);
		}
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		BlockBaker.render(BlockBaker.VACUUM_TOP);
		GlStateManager.popMatrix();
		//Middle
		GlStateManager.disableLighting();
		GLHelper.lightMap(255F, 255F);
		BlockBaker.render(BlockBaker.VACUUM_PIECE);
		GLHelper.lightMap(prevU, prevV);
		GlStateManager.enableLighting();
		//Bottom
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, -1F, 0F);
		BlockBaker.render(BlockBaker.VACUUM_BOTTOM);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
