/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.SpriteLibrary;
import arekkuusu.implom.client.util.helper.ProfilerHelper;
import arekkuusu.implom.common.block.tile.TileQSquared;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/*
 * Created by <Arekkuusu> on 17/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class QSquaredRenderer extends SpecialModelRenderer<TileQSquared> {

	@Override
	void renderTile(TileQSquared squared, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		RenderHelper.disableStandardItemLighting();
		renderModel(squared.tick, x, y, z);
		RenderHelper.enableStandardItemLighting();
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(Minecraft.getMinecraft().player.ticksExisted, x, y, z);
	}

	private void renderModel(float tick, double x, double y, double z) {
		ProfilerHelper.begin("[Q²] - Rendering waves");
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		calculateWaves(tick);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		ShaderLibrary.BRIGHT.end();
		ProfilerHelper.end();
	}

	private void calculateWaves(float tick) {
		double[] layers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		double perLayer = 0.00625D;
		double maxLayer = 0.025D;
		double modifier = MathHelper.sin(tick * 0.035F) * 24D;
		for(int wave = 0; wave < 2; wave++) {
			int num = wave == 0 ? (int) modifier : (int) (modifier + (-(modifier - 7D) * 2D));
			for(int i = num - 4; i < num + 4; i++) {
				if(i < layers.length && i >= 0) {
					int diff = i - num;
					if(diff > 0) diff = -diff;
					double layer = maxLayer + (perLayer * (double) diff);
					if(layer > layers[i]) {
						layers[i] = layer;
					}
				}
			}
		}
		SpriteLibrary.Q_SQUARED.bindManager();
		for(int layer = 0; layer < 16; layer++) {
			renderLayer(0.5F + layers[layer], layer);
		}
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
