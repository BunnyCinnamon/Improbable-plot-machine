/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ModelBakery;
import arekkuusu.solar.client.util.ModelBakery.BlockBaker;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.BlendHelper;
import arekkuusu.solar.client.util.resource.FrameSpriteResource;
import arekkuusu.solar.common.block.tile.TileCrystalVoid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import static arekkuusu.solar.client.util.ModelBakery.BlockBaker.PRIMAL_SIDE;

/**
 * Created by <Arekkuusu> on 27/08/2017.
 * It's distributed as part of Solar.
 */
public class CrystalVoidRenderer extends SpecialModelRenderer<TileCrystalVoid> {

	@Override
	void renderTile(TileCrystalVoid crystal, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = crystal.getStack();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.disableLighting();

		renderFloatingSquares(crystal.tick, partialTicks);
		renderGlyphs(crystal.tick, !stack.isEmpty());
		renderWhiteCube(crystal.tick, partialTicks);

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		if(!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableBlend();
			RenderItem render = Minecraft.getMinecraft().getRenderItem();
			GlStateManager.translate(x + 0.5D, y + 0.4D, z + 0.5D);

			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.rotate(partialTicks + crystal.tick % 360F, 0F, 1F, 0F);

			render.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.disableBlend();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void renderItem(double x, double y, double z, float partialTicks) {
		int tick = Minecraft.getMinecraft().player.ticksExisted;
		final float prevU = OpenGlHelper.lastBrightnessX;
		final float prevV = OpenGlHelper.lastBrightnessY;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.disableLighting();

		renderFloatingSquares(tick, partialTicks);
		renderGlyphs(tick, false);
		renderWhiteCube(tick, partialTicks);

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		BlendHelper.lightMap(prevU, prevV);
	}

	private void renderFloatingSquares(int age, float partialTicks) {
		GlStateManager.rotate(-age % 360, 0, 1, 0);
		GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians(partialTicks / 15f + -age / 15 % 360)), 1, 0, 0);
		GlStateManager.scale(0.75F, 0.75F, 0.75F);

		//Sides x 4
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		boolean side = true;
		int tick = age;
		for(int i = 0; i <= 90;) {
			for(int offset = i; offset <= 270; offset += i + 90, tick = side ? age : -age) {
				GlStateManager.pushMatrix();

				GlStateManager.rotate(offset, side ? 0F : 1F, side ? 1F : 0F, 0F);
				GlStateManager.rotate(tick, 0F, 0F, 1F);
				BlockBaker.render(PRIMAL_SIDE);

				GlStateManager.popMatrix();
			}
			side = false;
			i += 90;
		}
	}

	private void renderGlyphs(int tick, boolean active) {
		BlendHelper.lightMap(255F, 255F);
		GlStateManager.disableCull();

		FrameSpriteResource sprite = active ? SpriteLibrary.BLUE_GLYPH : SpriteLibrary.RED_GLYPH;
		sprite.bindManager();
		Tuple<Double, Double> uv = sprite.getUVFrame((int) (tick * 0.15F));
		double vOffset = sprite.getV();
		double v = uv.getSecond();

		ModelBakery.renderCube(0.47F, 0F, 1F, v, v + vOffset);
		GlStateManager.enableCull();
	}

	private void renderWhiteCube(int tick, float partialTicks) {
		GlStateManager.disableTexture2D();

		GlStateManager.rotate(tick % 360, 0, 1, 0);
		GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians(partialTicks / 3.0f + tick / 3 % 360)), 1, 0, 0);
		GlStateManager.scale(0.25F, 0.25F, 0.25F);
		ModelBakery.drawCube(0xFFFFFF, 1F);

		GlStateManager.enableTexture2D();
	}
}
