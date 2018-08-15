/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.client.util;

import arekkuusu.solar.common.Solar;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.katsstuff.teamnightclipse.mirror.client.shaders.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/*
 * Created by <Arekkuusu> on 08/10/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ShaderLibrary {

	public static final MirrorShaderProgram ALPHA = loadProgram(
			ResourceLibrary.BLEND_SHADER,
			ImmutableList.of(ShaderType.fragment(), ShaderType.vertex()),
			ImmutableMap.of("alpha", UniformType.unFloat())
	);
	public static final MirrorShaderProgram BRIGHT = loadProgram(
			ResourceLibrary.BRIGHT_SHADER,
			ImmutableList.of(ShaderType.fragment(), ShaderType.vertex()),
			ImmutableMap.of("brightness", UniformType.unFloat())
	);

	private static MirrorShaderProgram loadProgram(ResourceLocation location, List<ShaderType> shaders, Map<String, UniformType> types) {
		Map<String, UniformBase<? extends UniformType>> uniforms = Maps.newHashMap();
		types.forEach((k, v) -> uniforms.put(k, new UniformBase<>(v, 1)));
		return ShaderManager.createProgram(location, shaders, uniforms, true);
	}

	public static void init() {
		Solar.LOG.info("[BAKING PIE!]");
	}
}
