/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.BlendHelper;
import arekkuusu.solar.common.block.tile.TileQuantumMirror;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class QuantumMirrorRenderer extends TESRModelRenderer<TileQuantumMirror> {

	@Override
	void renderTile(TileQuantumMirror mirror, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!mirror.getWorld().isBlockLoaded(mirror.getPos(), false)) return;

		ItemStack stack = SolarApi.getQuantumItem(mirror.getKey(), 0);
		if(!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			BlendHelper.lightMap(255F, 255F);
			GlStateManager.translate(x + 0.5, y + 0.3, z + 0.5);

			GlStateManager.rotate(partialTicks + mirror.tick % 360F, 0F, 1F, 0F);

			RenderItem render = Minecraft.getMinecraft().getRenderItem();

			render.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.disableBlend();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
		}

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		float light = MathHelper.cos(mirror.tick * 0.05F);
		if(light < 0) light *= -1;
		light *= 255F;
		BlendHelper.lightMap(light, light);

		GlStateManager.translate(x, y, z + 0.5F);

		SpriteLibrary.QUANTUM_MIRROR.bindManager();
		renderMirror(mirror.tick, -180F, 0.5F);
		renderMirror(-mirror.tick, 90F, 0.75F);
		renderMirror(mirror.tick, 0F, 1F);

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public void renderModel(double x, double y, double z, float partialTicks) {
		int tick = Minecraft.getMinecraft().player.ticksExisted;
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;

		if(TESRModelRenderer.getTempItemRenderer() != null) {
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			BlendHelper.lightMap(255F, 255F);
			GlStateManager.translate(x + 0.5, y + 0.4, z + 0.5);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);

			GlStateManager.rotate(partialTicks + tick % 360F, 0F, 1F, 0F);

			RenderItem render = Minecraft.getMinecraft().getRenderItem();

			render.renderItem(TESRModelRenderer.getTempItemRenderer(), ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.disableBlend();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();

			TESRModelRenderer.setTempItemRenderer(null);
		}

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		float brigthness = MathHelper.cos(tick * 0.05F);
		if(brigthness < 0) brigthness *= -1;
		brigthness *= 255F;
		BlendHelper.lightMap(brigthness, brigthness);

		GlStateManager.translate(x, y, z + 0.5F);

		SpriteLibrary.QUANTUM_MIRROR.bindManager();
		renderMirror(tick, -180F, 0.25F);
		renderMirror(-tick, 90F, 0.375F);
		renderMirror(tick, 0F, 0.5F);

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		BlendHelper.lightMap(prevU, prevV);
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
