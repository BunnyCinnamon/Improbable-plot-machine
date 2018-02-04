/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.util.resource.ShaderManager;
import arekkuusu.solar.client.util.resource.shader.ShaderResource;
import arekkuusu.solar.common.Solar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 08/10/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ShaderLibrary {

	public static final ShaderResource ALPHA = ShaderManager.load("alpha", "alpha");

	public static void init() {
		Solar.LOG.info("[BAKING PIE!]");
	}
}
