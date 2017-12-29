/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.RenderBakery;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.GLHelper;
import arekkuusu.solar.common.block.tile.TileQuingentilliard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class QuingentilliardRenderer extends SpecialModelRenderer<TileQuingentilliard> {

	@Override
	void renderTile(TileQuingentilliard te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int layer = MinecraftForgeClient.getRenderPass();

		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
		switch(layer) {
			case 0:
				if(!te.getLookup().isEmpty()) {
					GlStateManager.pushMatrix();
					GlStateManager.disableLighting();

					GLHelper.lightMap(255F, 255F);
					GlStateManager.translate(x + 0.5, y + 1F, z + 0.5);
					GlStateManager.scale(0.5F, 0.5F, 0.5F);
					RenderBakery.makeUpDownTranslation(te.tick, 0.1F, 1F, 0F);

					GlStateManager.rotate(partialTicks + (float) te.tick * 0.5F % 360F, 0F, 1F, 0F);
					RenderBakery.renderItemStack(te.getLookup());

					GlStateManager.enableLighting();
					GlStateManager.popMatrix();
				}
				break;
			case 1:
				render(te.tick, x, y, z, false);
				break;
		}
		GLHelper.lightMap(prevU, prevV);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;
		render(Minecraft.getMinecraft().player.ticksExisted, x, y, z, true);
		GLHelper.lightMap(prevU, prevV);
	}

	public void render(int tick, double x, double y, double z, boolean isGui) {
		GLHelper.lightMap(255F, 255F);
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.color(0, 0.99609375F, 0.76171875F, 1F);

		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(tick, 1F, 0F, 1F);

		GLHelper.disableDepth();
		if(!isGui)
			GlStateManager.enableBlend();
		GLHelper.BLEND_NORMAL.blend();
		RenderBakery.renderBeams((float) tick * 0.01F, 25, 0x000000, 0x000000, 1F);
		renderCube(tick);
		if(!isGui)
			GlStateManager.disableBlend();
		GLHelper.enableDepth();

		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	private void renderCube(int age) {
		SpriteLibrary.QUINGENTILLIARD.bindManager();
		Tuple<Double, Double> uv = SpriteLibrary.QUINGENTILLIARD.getUVFrame((int) (age * 0.25F));
		double v = uv.getSecond();
		//UV
		float uMin = 0.28125F;
		float uMax = 0.71875F;
		float vMin = 0.03515625F + (float) v;
		float vMax = 0.08984375F + (float) v;

		RenderBakery.renderCube(0.35F, uMin, uMax, vMin, vMax);
	}
}
