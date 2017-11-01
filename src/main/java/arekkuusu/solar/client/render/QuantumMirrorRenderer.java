/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.api.entanglement.quantum.QuantumHandler;
import arekkuusu.solar.client.util.RenderBakery;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.GLHelper;
import arekkuusu.solar.common.block.tile.TileQuantumMirror;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class QuantumMirrorRenderer extends SpecialModelRenderer<TileQuantumMirror> {

	@Override
	void renderTile(TileQuantumMirror mirror, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int layer = MinecraftForgeClient.getRenderPass();

		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
		switch(layer) {
			case 0:
				Optional<UUID> optional = mirror.getKey();
				if(optional.isPresent()) {
					ItemStack stack = QuantumHandler.getQuantumStack(optional.get(), 0);
					if(stack.isEmpty()) break;

					GlStateManager.pushMatrix();
					GLHelper.lightMap(255F, 255F);
					GlStateManager.translate(x + 0.5, y + 0.38, z + 0.5);

					GlStateManager.rotate(partialTicks + (float) mirror.tick * 0.5F % 360F, 0F, 1F, 0F);
					RenderBakery.renderItemStack(stack);

					GlStateManager.popMatrix();
				}
				break;
			case 1:
				renderModel(mirror.tick, x, y, z);
				break;
		}
		GLHelper.lightMap(prevU, prevV);
	}

	@Override
	public void renderStack(double x, double y, double z, float partialTicks) {
		int tick = Minecraft.getMinecraft().player.ticksExisted;
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;

		ItemStack stack = SpecialModelRenderer.getTempItemRenderer();
		if(stack != null) {
			GlStateManager.pushMatrix();
			GLHelper.lightMap(255F, 255F);
			GlStateManager.translate(x + 0.5D, y + 0.4D, z + 0.5D);

			GlStateManager.rotate(partialTicks + (float) tick * 0.5F % 360F, 0F, 1F, 0F);
			RenderBakery.renderItemStack(stack);

			GlStateManager.popMatrix();
		}

		GLHelper.disableDepth();
		renderModel(tick, x, y, z);
		GLHelper.enableDepth();
		GLHelper.lightMap(prevU, prevV);
	}

	private void renderModel(int tick, double x, double y, double z) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GLHelper.BLEND_SRC_ALPHA$ONE.blend();

		float brigthness = MathHelper.cos(tick * 0.05F);
		if(brigthness < 0) brigthness *= -1;
		brigthness *= 255F;
		GLHelper.lightMap(brigthness, brigthness);

		GlStateManager.translate(x, y, z + 0.5F);

		SpriteLibrary.QUANTUM_MIRROR.bindManager();
		renderMirror(tick, -180F, 0.5F);
		renderMirror(-tick, 90F, 0.75F);
		renderMirror(tick, 0F, 1F);

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	private void renderMirror(int age, float offset, float scale) {
		GlStateManager.pushMatrix();
		Tuple<Double, Double> uv = SpriteLibrary.QUANTUM_MIRROR.getUVFrame((int) (age * 0.25F));
		double vOffset = SpriteLibrary.QUANTUM_MIRROR.getV();
		double v = uv.getSecond();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();

		GlStateManager.translate(0.5F, 0.5F, 0F);

		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(offset - age, 0F, 1F, 0F);
		GlStateManager.rotate(offset - age, 1F, 0F, 0F);
		GlStateManager.rotate(offset - age, 0F, 0F, 1F);

		GlStateManager.translate(-0.5F, -0.5F, 0F);

		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		buff.pos(0, 0, 0).tex(0, v).endVertex();
		buff.pos(1, 0, 0).tex(1, v).endVertex();
		buff.pos(1, 1, 0).tex(1, v + vOffset).endVertex();
		buff.pos(0, 1, 0).tex(0, v + vOffset).endVertex();

		tessellator.draw();
		GlStateManager.popMatrix();
	}
}
