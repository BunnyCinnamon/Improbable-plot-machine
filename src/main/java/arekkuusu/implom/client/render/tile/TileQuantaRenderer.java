/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.SpriteLibrary;
import arekkuusu.implom.client.util.helper.ProfilerHelper;
import arekkuusu.implom.client.util.sprite.UVFrame;
import arekkuusu.implom.common.block.tile.TileQuanta;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
public class TileQuantaRenderer extends TileEntitySpecialRenderer<TileQuanta> {

	@Override
	public void render(TileQuanta quanta, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		RenderHelper.disableStandardItemLighting();
		renderModel(quanta.tick, x, y, z);
		RenderHelper.enableStandardItemLighting();
	}

	public static void renderModel(float tick, double x, double y, double z) {
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
		renderWaves(tick * 0.035F);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		ShaderLibrary.BRIGHT.end();
		ProfilerHelper.end();
	}

	private static void renderWaves(float tick) {
		double[] layers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		double perLayer = 0.00625D;
		double maxLayer = 0.025D;
		double modifier = MathHelper.sin(tick) * 24D;
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
		SpriteLibrary.QUANTA.bind();
		UVFrame frame = SpriteLibrary.QUANTA.getFrame();
		for(int layer = 0; layer < 16; layer++) {
			renderLayer(frame, 0.5F + layers[layer], layer);
		}
	}

	private static void renderLayer(UVFrame frame, double size, int layer) {
		double offset = (frame.vMin - frame.vMax) / 16D;
		//UV
		double vMin = frame.vMin - offset * (layer + 1);
		double vMax = vMin + offset;
		double uMin = frame.uMin;
		double uMax = frame.uMax;
		//POS
		double yMax = 0.5D - 0.0625D * (double) layer;
		double yMin = yMax - 0.0625D;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		//Front
		buff.pos(size, yMin, -size).tex(uMax, vMin).endVertex();
		buff.pos(size, yMax, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, yMax, -size).tex(uMin, vMax).endVertex();
		buff.pos(-size, yMin, -size).tex(uMin, vMin).endVertex();
		//Back
		buff.pos(-size, yMin, size).tex(uMax, vMin).endVertex();
		buff.pos(-size, yMax, size).tex(uMax, vMax).endVertex();
		buff.pos(size, yMax, size).tex(uMin, vMax).endVertex();
		buff.pos(size, yMin, size).tex(uMin, vMin).endVertex();
		//Right
		buff.pos(size, yMin, size).tex(uMax, vMin).endVertex();
		buff.pos(size, yMax, size).tex(uMax, vMax).endVertex();
		buff.pos(size, yMax, -size).tex(uMin, vMax).endVertex();
		buff.pos(size, yMin, -size).tex(uMin, vMin).endVertex();
		//Left
		buff.pos(-size, yMin, -size).tex(uMax, vMin).endVertex();
		buff.pos(-size, yMax, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, yMax, size).tex(uMin, vMax).endVertex();
		buff.pos(-size, yMin, size).tex(uMin, vMin).endVertex();
		//Top
		buff.pos(-size, yMax, -size).tex(frame.uMax, frame.vMin).endVertex();
		buff.pos(size, yMax, -size).tex(frame.uMax, frame.vMax).endVertex();
		buff.pos(size, yMax, size).tex(frame.uMin, frame.vMax).endVertex();
		buff.pos(-size, yMax, size).tex(frame.uMin, frame.vMin).endVertex();
		//Bottom
		buff.pos(-size, yMin, -size).tex(frame.uMax, frame.vMin).endVertex();
		buff.pos(size, yMin, -size).tex(frame.uMax, frame.vMax).endVertex();
		buff.pos(size, yMin, size).tex(frame.uMin, frame.vMax).endVertex();
		buff.pos(-size, yMin, size).tex(frame.uMin, frame.vMin).endVertex();
		tessellator.draw();
	}
}
