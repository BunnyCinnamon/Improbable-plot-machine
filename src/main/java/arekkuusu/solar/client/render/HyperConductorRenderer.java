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
import arekkuusu.solar.common.block.tile.TileHyperConductor;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 17/10/2017.
 * It's distributed as part of Solar.
 */
public class HyperConductorRenderer extends SpecialModelRenderer<TileHyperConductor> {

	@Override
	void renderTile(TileHyperConductor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(x, y, z, partialTicks);
	}

	private void renderModel(double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks) * 0.5F;
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		GlStateManager.rotate(tick % 360, 0, 1, tick % 720);
		GlStateManager.rotate(15.0F * (float) Math.sin(Math.toRadians(partialTicks / 15F + tick / 15F % 360F)), 1, 0, 0);
		GlStateManager.rotate(45.0F * (float) Math.sin(Math.toRadians(partialTicks / 15F + tick / 15F % 360F)), 0, 1, 0);
		GlStateManager.rotate(30.0F * (float) Math.sin(Math.toRadians(partialTicks / 15F + tick / 15F % 360F)), 0, 0, 1);

		for(int layer = 0; layer < 3; layer++) {
			float relativeTime = (tick - ((float) layer * 145F)) * 0.05F;

			float size = MathHelper.sin(relativeTime);
			size = (size < 0 ? -size : size);
			size += 0.2F;
			size *= 0.4F;

			double uvMin = 0.3125D;
			SpriteLibrary.HYPER_CONDUCTOR.bindManager();
			renderCube(size, uvMin, 1 - uvMin, uvMin, 1 - uvMin);

			SpriteLibrary.HYPER_CONDUCTOR_OVERLAY.bindManager();
			Tuple<Double, Double> uv = SpriteLibrary.HYPER_CONDUCTOR_OVERLAY.getUVFrame((int) (tick * 0.45F));
			double uOffset = SpriteLibrary.HYPER_CONDUCTOR_OVERLAY.getU();
			double u = uv.getFirst();
			double vOffset = SpriteLibrary.HYPER_CONDUCTOR_OVERLAY.getV();
			double v = uv.getSecond();

			double uMin = u + uvMin / 2F;
			double uMax = u + uOffset - uvMin / 2F;
			double vMin = v + uvMin / 4F;
			double vMax = v + vOffset - uvMin / 4F;

			final float prevU = OpenGlHelper.lastBrightnessX;
			final float prevV = OpenGlHelper.lastBrightnessY;
			GlStateManager.disableLighting();
			GLHelper.lightMap(255F, 255F);
			renderCube(size, uMin, uMax, vMin, vMax);
			GLHelper.lightMap(prevU, prevV);
			GlStateManager.enableLighting();
		}

		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	private void renderCube(float size, double uMin, double uMax, double vMin, double vMax) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		size = 0.5F * size;
		//Front
		buff.pos(size, -size, -size).tex(uMax, vMin).endVertex();
		buff.pos(size, size, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, size, -size).tex(uMin, vMax).endVertex();
		buff.pos(-size, -size, -size).tex(uMin, vMin).endVertex();
		//Back
		buff.pos(-size, -size, size).tex(uMax, vMin).endVertex();
		buff.pos(-size, size, size).tex(uMax, vMax).endVertex();
		buff.pos(size, size, size).tex(uMin, vMax).endVertex();
		buff.pos(size, -size, size).tex(uMin, vMin).endVertex();
		//Right
		buff.pos(size, -size, size).tex(uMax, vMin).endVertex();
		buff.pos(size, size, size).tex(uMax, vMax).endVertex();
		buff.pos(size, size, -size).tex(uMin, vMax).endVertex();
		buff.pos(size, -size, -size).tex(uMin, vMin).endVertex();
		//Left
		buff.pos(-size, -size, -size).tex(uMax, vMin).endVertex();
		buff.pos(-size, size, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, size, size).tex(uMin, vMax).endVertex();
		buff.pos(-size, -size, size).tex(uMin, vMin).endVertex();
		//Top
		buff.pos(-size, size, -size).tex(uMax, vMin).endVertex();
		buff.pos(size, size, -size).tex(uMax, vMax).endVertex();
		buff.pos(size, size, size).tex(uMin, vMax).endVertex();
		buff.pos(-size, size, size).tex(uMin, vMin).endVertex();
		//Bottom
		buff.pos(-size, -size, -size).tex(uMax, vMin).endVertex();
		buff.pos(-size, -size, size).tex(uMin, vMin).endVertex();
		buff.pos(size, -size, size).tex(uMin, vMax).endVertex();
		buff.pos(size, -size, -size).tex(uMax, vMax).endVertex();

		tessellator.draw();
	}
}
