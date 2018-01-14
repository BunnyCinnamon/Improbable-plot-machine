/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.GLHelper;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileGravityInhibitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;

/**
 * Created by <Arekkuusu> on 17/10/2017.
 * It's distributed as part of Solar.
 */
public class GravityInhibitorRenderer extends SpecialModelRenderer<TileGravityInhibitor> {

	@Override
	void renderTile(TileGravityInhibitor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(x, y, z, partialTicks);
	}

	private void renderModel(double x, double y, double z, float partialTicks) {
		int tick = (int) Minecraft.getMinecraft().world.getTotalWorldTime();
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		GlStateManager.rotate(tick % 360, 0, 1, tick % 720);
		GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians(partialTicks / 15f + tick / 15 % 360)), 1, 0, 0);

		for(int layer = 0; layer < 3; layer++) {
			float relativeTime = ((float) tick - ((float) layer * 145F)) * 0.05F;

			float size = MathHelper.sin(relativeTime);
			size = (size < 0 ? -size : size);
			size += 0.2F;
			size *= 0.4F;

			double uvMin = 0.3125D;
			SpriteLibrary.GRAVITY_INHIBITOR.bindManager();
			RenderHelper.renderCube(size, uvMin, 1 - uvMin, uvMin, 1 - uvMin);

			SpriteLibrary.GRAVITY_INHIBITOR_OVERLAY.bindManager();
			Tuple<Double, Double> uv = SpriteLibrary.GRAVITY_INHIBITOR_OVERLAY.getUVFrame((int) ((float) tick * 0.45F));
			double uOffset = SpriteLibrary.GRAVITY_INHIBITOR_OVERLAY.getU();
			double u = uv.getFirst();
			double vOffset = SpriteLibrary.GRAVITY_INHIBITOR_OVERLAY.getV();
			double v = uv.getSecond();

			double uMin = u + uvMin / 2F;
			double uMax = u + uOffset - uvMin / 2F;
			double vMin = v + uvMin / 4F;
			double vMax = v + vOffset - uvMin / 4F;

			final float prevU = OpenGlHelper.lastBrightnessX;
			final float prevV = OpenGlHelper.lastBrightnessY;
			GlStateManager.disableLighting();
			GLHelper.lightMap(255F, 255F);
			RenderHelper.renderCube(size, uMin, uMax, vMin, vMax);
			GLHelper.lightMap(prevU, prevV);
			GlStateManager.enableLighting();
		}

		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}
}
