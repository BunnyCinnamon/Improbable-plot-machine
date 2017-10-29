/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.util.ResourceLibrary.AssetLocation;
import arekkuusu.solar.client.util.ResourceLibrary.ShaderLocation;
import arekkuusu.solar.common.Solar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * Created by <Arekkuusu> on 08/10/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ShaderLibrary {

	public static final ShaderGroup REFRACTION = loadShader("refraction");

	@SuppressWarnings("ConstantConditions")
	private static ShaderGroup loadShader(String name) {
		if(ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
			ShaderLinkHelper.setNewStaticShaderLinkHelper();
		}

		ResourceLocation location = ResourceLibrary.getLocation(AssetLocation.SHADERS, ShaderLocation.POST, name, ".json");
		Minecraft mc = Minecraft.getMinecraft();

		ShaderGroup shader = null;
		try {
			shader = new ShaderGroup(mc.renderEngine, mc.getResourceManager(), mc.getFramebuffer(), location);
			shader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
		} catch(IOException e) {
			Solar.LOG.fatal("[ShaderLibrary] Failed to load shader: " + location.toString());
			e.printStackTrace();
		}

		return shader;
	}

	public static void init() {}
}
