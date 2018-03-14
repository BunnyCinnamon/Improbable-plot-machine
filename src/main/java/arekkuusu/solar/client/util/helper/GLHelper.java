/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.helper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public enum GLHelper {
	BLEND_NORMAL(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA),
	BLEND_ALPHA(GL11.GL_ONE, GL11.GL_SRC_ALPHA),
	BLEND_PRE_ALPHA(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA),
	BLEND_MULTIPLY(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA),
	BLEND_ADDITIVE(GL11.GL_ONE, GL11.GL_ONE),
	BLEND_ADDITIVE_DARK(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR),
	BLEND_OVERLAY_DARK(GL11.GL_SRC_COLOR, GL11.GL_ONE),
	BLEND_ADDITIVE_ALPHA(GL11.GL_SRC_ALPHA, GL11.GL_ONE),
	BLEND_INVERTED_ADD(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR),
	BLEND_SRC_ALPHA$ONE(SourceFactor.SRC_ALPHA, DestFactor.ONE),
	BLEND_SRC_ALPHA$ONE_MINUS_SRC_ALPHA(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

	private final int gl0, gl1;

	GLHelper(int gl0, int gl1) {
		this.gl0 = gl0;
		this.gl1 = gl1;
	}

	GLHelper(SourceFactor factor, DestFactor dest) {
		this.gl0 = factor.factor;
		this.gl1 = dest.factor;
	}

	public void blend() {
		GlStateManager.blendFunc(gl0, gl1);
	}

	public static void lightMap(float u, float v) {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, u, v);
	}

	public static void disableDepth() {
		GlStateManager.depthMask(false);
	}

	public static void enableDepth() {
		GlStateManager.depthMask(true);
	}
}
