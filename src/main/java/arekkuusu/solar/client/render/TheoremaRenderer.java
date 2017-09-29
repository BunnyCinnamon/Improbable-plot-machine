/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.helper.BlendHelper;
import arekkuusu.solar.common.block.tile.TileTheorema;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 22/09/2017.
 * It's distributed as part of Solar.
 */
public class TheoremaRenderer extends SpecialModelRenderer<TileTheorema> { //Taken from the End Portal

	private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
	private static final FloatBuffer VIEW = GLAllocation.createDirectFloatBuffer(16);
	private static final Random RANDOM = new Random(31100L);

	private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

	@Override
	void renderTile(TileTheorema pandora, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(pandora, x, y, z);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(null, x, y, z);
	}

	private void renderModel(@Nullable TileTheorema pandora, double x, double y, double z) {
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
		BlendHelper.lightMap(255F, 255F);

		RANDOM.setSeed(31100L);

		GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
		GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
		GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
		GlStateManager.texGen(GlStateManager.TexGen.S, 9474, getBuffer(1F, 0F, 0F));
		GlStateManager.texGen(GlStateManager.TexGen.T, 9474, getBuffer(0F, 1F, 0F));
		GlStateManager.texGen(GlStateManager.TexGen.R, 9474, getBuffer(0F, 0F, 1F));
		GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);

		GlStateManager.disableLighting();
		GlStateManager.getFloat(2982, VIEW);
		GlStateManager.getFloat(2983, PROJECTION);
		double squared = x * x + y * y + z * z;
		int passes = getPasses(squared);
		boolean fog = false;

		for(int pass = 0; pass < passes; ++pass) {
			GlStateManager.pushMatrix();
			bindTexture(ResourceLibrary.THEOREMA);
			float colorOffset = pass == 0 ? 0F : 2.0F / (float) (16 - pass);

			if(pass == 1) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			} else
			if(pass == 0) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			} else {
				Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
				fog = true;
			}

			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.5F, 0.5F, 0F);
			GlStateManager.scale(0.5F, 0.5F, 1F);
			float passOffset = (float) (pass + 1);
			GlStateManager.translate(17F / passOffset, (2F + passOffset / 1.5F) * ((float) Minecraft.getSystemTime() % 800000F / 800000F), 0F);
			GlStateManager.rotate((passOffset * passOffset * 4321F + passOffset * 9F) * 2F, 0F, 0F, 1F);
			GlStateManager.scale(4.5F - passOffset / 6F, 4.5F - passOffset / 6F, 1F);
			GlStateManager.multMatrix(PROJECTION);
			GlStateManager.multMatrix(VIEW);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buff = tessellator.getBuffer();
			buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			float mod = RANDOM.nextFloat();
			float r = (mod * 0.75F) * colorOffset;
			float g = (mod * 0.75F) * colorOffset;
			float b = (mod * 1F) * colorOffset;

			if(pandora == null || pandora.shouldRenderFace(EnumFacing.SOUTH)) {
				buff.pos(x, y, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y + 1D, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x, y + 1D, z + 1D).color(r, g, b, 1F).endVertex();
			}

			if(pandora == null || pandora.shouldRenderFace(EnumFacing.NORTH)) {
				buff.pos(x, y + 1D, z).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y + 1D, z).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y, z).color(r, g, b, 1F).endVertex();
				buff.pos(x, y, z).color(r, g, b, 1F).endVertex();
			}

			if(pandora == null || pandora.shouldRenderFace(EnumFacing.EAST)) {
				buff.pos(x + 1D, y + 1D, z).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y + 1D, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y, z).color(r, g, b, 1F).endVertex();
			}

			if(pandora == null || pandora.shouldRenderFace(EnumFacing.WEST)) {
				buff.pos(x, y, z).color(r, g, b, 1F).endVertex();
				buff.pos(x, y, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x, y + 1D, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x, y + 1D, z).color(r, g, b, 1F).endVertex();
			}

			if(pandora == null || pandora.shouldRenderFace(EnumFacing.DOWN)) {
				buff.pos(x, y, z).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y, z).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x, y, z + 1D).color(r, g, b, 1F).endVertex();
			}

			if(pandora == null || pandora.shouldRenderFace(EnumFacing.UP)) {
				buff.pos(x, y + 1D, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y + 1D, z + 1D).color(r, g, b, 1F).endVertex();
				buff.pos(x + 1D, y + 1D, z).color(r, g, b, 1F).endVertex();
				buff.pos(x, y + 1D, z).color(r, g, b, 1F).endVertex();
			}

			tessellator.draw();
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}

		GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.enableLighting();
		BlendHelper.lightMap(prevU, prevV);

		if(fog) {
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		}
	}

	private int getPasses(double squared) {
		int passes;

		if(squared > 36864D) {
			passes = 1;
		} else if(squared > 25600D) {
			passes = 3;
		} else if(squared > 16384D) {
			passes = 5;
		} else if(squared > 9216D) {
			passes = 7;
		} else if(squared > 4096D) {
			passes = 9;
		} else if(squared > 1024D) {
			passes = 11;
		} else if(squared > 576D) {
			passes = 13;
		} else if(squared > 256D) {
			passes = 14;
		} else {
			passes = 15;
		}

		return passes;
	}

	private FloatBuffer getBuffer(float v1, float v2, float v3) {
		buffer.clear();
		buffer.put(v1).put(v2).put(v3).put(0F);
		buffer.flip();
		return buffer;
	}
}
