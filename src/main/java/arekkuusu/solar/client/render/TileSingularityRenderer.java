/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.RenderBakery;
import arekkuusu.solar.client.util.helper.BlendHelper;
import arekkuusu.solar.common.block.tile.TileSingularity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class TileSingularityRenderer extends TileEntitySpecialRenderer<TileSingularity> {

	@Override
	public void render(TileSingularity singularity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!singularity.getWorld().isBlockLoaded(singularity.getPos(), false)) return;

		GlStateManager.pushMatrix();
		BlendHelper.lightMap(255F, 255F);
		GlStateManager.disableLighting();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);

		GlStateManager.pushMatrix();
		renderCube(singularity.tick, partialTicks);
		GlStateManager.popMatrix();

		GlStateManager.enableBlend();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GlStateManager.disableCull();
		BlendHelper.BLEND_NORMAL.blend();

		RenderBakery.renderBeams((float) singularity.tick * 0.001F, 30, 0xFFFFFF, 0xFFFFFF, 0.5F);

		GlStateManager.enableCull();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GlStateManager.disableBlend();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	private void renderCube(int age, float partialTicks) {
		GlStateManager.disableTexture2D();

		GlStateManager.rotate(age % 360, 0, 1, 0);
		GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians(partialTicks / 3.0f + age / 3 % 360)), 1, 0, 0);
		GlStateManager.scale(0.25F, 0.25F, 0.25F);
		RenderBakery.drawCube(0xFFFFFF, 1F);

		GlStateManager.enableTexture2D();
	}
}
