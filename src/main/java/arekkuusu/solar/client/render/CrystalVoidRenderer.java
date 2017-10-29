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
import arekkuusu.solar.client.util.helper.BlockBaker;
import arekkuusu.solar.client.util.resource.FrameSpriteResource;
import arekkuusu.solar.common.block.tile.TileCrystalVoid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static arekkuusu.solar.client.util.helper.BlockBaker.PRIMAL_SIDE;

/**
 * Created by <Arekkuusu> on 27/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class CrystalVoidRenderer extends SpecialModelRenderer<TileCrystalVoid> {

	@Override
	void renderTile(TileCrystalVoid crystal, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int layer = MinecraftForgeClient.getRenderPass();

		switch(layer) {
			case 0:
				ItemStack stack = crystal.getStack();

				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
				GlStateManager.disableLighting();

				renderFloatingSquares(crystal.tick, partialTicks);
				renderGlyphs(crystal.tick, !stack.isEmpty());

				GlStateManager.enableLighting();
				GlStateManager.popMatrix();

				if(!stack.isEmpty()) {
					GlStateManager.pushMatrix();
					GlStateManager.enableRescaleNormal();
					GlStateManager.enableBlend();
					GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

					GlStateManager.scale(0.5F, 0.5F, 0.5F);
					GlStateManager.rotate(partialTicks + crystal.tick % 360F, 0F, 1F, 0F);

					RenderBakery.renderItemStack(stack);

					GlStateManager.disableBlend();
					GlStateManager.disableRescaleNormal();
					GlStateManager.popMatrix();
				}
				break;
			case 1:
				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
				GLHelper.lightMap(255F, 255F);
				GlStateManager.disableLighting();
				renderBeams(crystal.tick);
				GlStateManager.enableLighting();
				GlStateManager.popMatrix();
				break;
		}
	}

	@Override
	public void renderStack(double x, double y, double z, float partialTicks) {
		int tick = Minecraft.getMinecraft().player.ticksExisted;
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.disableLighting();

		renderFloatingSquares(tick, partialTicks);
		renderGlyphs(tick, false);
		GLHelper.disableDepth();
		renderBeams(tick);
		GLHelper.enableDepth();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		GLHelper.lightMap(prevU, prevV);
	}

	private void renderFloatingSquares(int age, float partialTicks) {
		GlStateManager.rotate(-age % 360, 0, 1, 0);
		GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians(partialTicks / 15f + -age / 15 % 360)), 1, 0, 0);
		GlStateManager.scale(0.75F, 0.75F, 0.75F);

		//Sides x 4
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		int tick = age;
		for(int i = 0; i <= 90;) {
			boolean side = i == 0;
			for(int offset = i; offset <= 270; offset += i + 90, tick = side ? age : -age) {
				GlStateManager.pushMatrix();

				GlStateManager.rotate(offset, side ? 0F : 1F, side ? 1F : 0F, 0F);
				GlStateManager.rotate(tick, 0F, 0F, 1F);
				BlockBaker.render(PRIMAL_SIDE);

				GlStateManager.popMatrix();
			}
			i += 90;
		}
	}

	private void renderGlyphs(int tick, boolean active) {
		GLHelper.lightMap(255F, 255F);

		FrameSpriteResource sprite = active ? SpriteLibrary.BLUE_GLYPH : SpriteLibrary.RED_GLYPH;
		sprite.bindManager();
		Tuple<Double, Double> uv = sprite.getUVFrame((int) (tick * 0.15F));
		double vOffset = sprite.getV();
		double v = uv.getSecond();

		GlStateManager.disableCull();
		RenderBakery.renderCube(0.47F, 0F, 1F, v, v + vOffset);
		GlStateManager.enableCull();
	}

	private void renderBeams(int tick) {
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		GLHelper.BLEND_NORMAL.blend();

		RenderBakery.renderBeams((float) tick * 0.001F, 30, 0xFFFFFF, 0xFFFFFF, 0.18F);

		GlStateManager.enableCull();
		GlStateManager.disableBlend();
	}
}
