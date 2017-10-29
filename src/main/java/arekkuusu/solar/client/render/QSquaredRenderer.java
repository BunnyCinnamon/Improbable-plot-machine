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
import arekkuusu.solar.common.block.tile.TileQSquared;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 17/09/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class QSquaredRenderer extends SpecialModelRenderer<TileQSquared> {

	@Override
	void renderTile(TileQSquared squared, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(squared.tick, x, y, z);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		int tick = Minecraft.getMinecraft().player.ticksExisted;
		renderModel(tick, x, y, z);
	}

	private void renderModel(int tick, double x, double y, double z) {
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
		GLHelper.lightMap(255F, 255F);

		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

		double[] layers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		double perLayer = 0.00625D;
		double maxLayer = 0.025D;
		double modifier = MathHelper.sin((float) tick * 0.035F);
		int num = (int) (24D * modifier);

		for(int i = num - 4; i < num + 4; i++) {
			if(i < layers.length && i >= 0) {
				int diff = i - num;
				if(diff > 0) diff = -diff;
				layers[i] = maxLayer + (perLayer * (double) diff);
			}
		}

		SpriteLibrary.Q_SQUARED.bindManager();
		for(int layer = 0; layer < 16; layer++) {
			renderLayer(0.5F + layers[layer], layer);
		}

		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
		GLHelper.lightMap(prevU, prevV);
	}

	private void renderLayer(double size, int layer) {
		double min = 0.0625D;

		//UV
		double vMin = min * (double) layer;
		double vMax = vMin + min;
		double uMin = 0;
		double uMax = 1D;
		//POS
		double yMin = 0.5D - vMin;
		double yMax = -yMin + min;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		//Front
		buff.pos(size, -yMin, -size).tex(uMax, vMin).endVertex();
		buff.pos(size, yMax, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, yMax, -size).tex(uMin, vMax).endVertex();
		buff.pos(-size, -yMin, -size).tex(uMin, vMin).endVertex();
		//Back
		buff.pos(-size, -yMin, size).tex(uMax, vMin).endVertex();
		buff.pos(-size, yMax, size).tex(uMax, vMax).endVertex();
		buff.pos(size, yMax, size).tex(uMin, vMax).endVertex();
		buff.pos(size, -yMin, size).tex(uMin, vMin).endVertex();
		//Right
		buff.pos(size, -yMin, size).tex(uMax, vMin).endVertex();
		buff.pos(size, yMax, size).tex(uMax, vMax).endVertex();
		buff.pos(size, yMax, -size).tex(uMin, vMax).endVertex();
		buff.pos(size, -yMin, -size).tex(uMin, vMin).endVertex();
		//Left
		buff.pos(-size, -yMin, -size).tex(uMax, vMin).endVertex();
		buff.pos(-size, yMax, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, yMax, size).tex(uMin, vMax).endVertex();
		buff.pos(-size, -yMin, size).tex(uMin, vMin).endVertex();
		//Top
		buff.pos(-size, yMax, -size).tex(uMax, uMin).endVertex();
		buff.pos(size, yMax, -size).tex(uMax, uMax).endVertex();
		buff.pos(size, yMax, size).tex(uMin, uMax).endVertex();
		buff.pos(-size, yMax, size).tex(uMin, uMin).endVertex();
		//Bottom
		buff.pos(-size, -yMin, -size).tex(uMax, uMin).endVertex();
		buff.pos(size, -yMin, -size).tex(uMax, uMax).endVertex();
		buff.pos(size, -yMin, size).tex(uMin, uMax).endVertex();
		buff.pos(-size, -yMin, size).tex(uMin, uMin).endVertex();

		tessellator.draw();
	}
}
