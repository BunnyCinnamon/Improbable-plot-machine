/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.util.helper;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public enum BlendHelper {
	BLEND_NORMAL(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA),
	BLEND_ALPHA(GL11.GL_ONE, GL11.GL_SRC_ALPHA),
	BLEND_PREALPHA(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA),
	BLEND_MULTIPLY(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA),
	BLEND_ADDITIVE(GL11.GL_ONE, GL11.GL_ONE),
	BLEND_ADDITIVEDARK(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR),
	BLEND_OVERLAYDARK(GL11.GL_SRC_COLOR, GL11.GL_ONE),
	BLEND_ADDITIVE_ALPHA(GL11.GL_SRC_ALPHA, GL11.GL_ONE),
	BLEND_INVERTEDADD(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);

	private final int gl0, gl1;

	BlendHelper(int gl0, int gl1) {
		this.gl0 = gl0;
		this.gl1 = gl1;
	}

	public void blend() {
		GL11.glBlendFunc(gl0, gl1);
	}

	public static void lightMap(float u, float v) {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, u, v);
	}
}
