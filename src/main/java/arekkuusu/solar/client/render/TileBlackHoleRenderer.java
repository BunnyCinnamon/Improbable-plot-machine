/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ModelBakery;
import arekkuusu.solar.client.util.helper.BlendHelper;
import arekkuusu.solar.common.block.tile.TileBlackHole;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 29/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class TileBlackHoleRenderer extends TileEntitySpecialRenderer<TileBlackHole> {

	@Override
	public void render(TileBlackHole hole, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!hole.getWorld().isBlockLoaded(hole.getPos(), false)) return;

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		BlendHelper.lightMap(255F, 255F);
		GlStateManager.disableLighting();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

		if(hole.suck) {
			BlendHelper.lightMap(255F, 255F);

			float scale = (float) hole.size / 80F;
			GlStateManager.scale(scale, scale, scale);
			renderAbyss(hole.tick, partialTicks);
		} else {
			float scale = (40F - (float) hole.tick) / 40F;
			GlStateManager.scale(scale, scale, scale);

			GlStateManager.enableBlend();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			BlendHelper.BLEND_NORMAL.blend();

			ModelBakery.renderBeams((float) hole.tick * 0.01F, 40, 0xFFFFFF, 0xFFFFFF, 1F);

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GlStateManager.disableBlend();
		}

		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}

	private void renderAbyss(int age, float partialTicks) {
		GlStateManager.disableTexture2D();

		GlStateManager.rotate(partialTicks + age % 360, 0, 1, 0);
		GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians(partialTicks / 3.0f + age / 3 % 360)), 1, 0, 0);
		ModelBakery.drawSphere(0, 1F);

		GlStateManager.enableTexture2D();
	}
}
