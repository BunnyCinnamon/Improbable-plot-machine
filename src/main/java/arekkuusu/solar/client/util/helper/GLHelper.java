/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.client.util.helper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class GLHelper {

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
