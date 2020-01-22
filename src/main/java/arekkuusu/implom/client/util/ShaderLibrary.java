/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util;

import arekkuusu.implom.common.IPM;
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
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public final class ShaderLibrary {

	//FRAGMENT & VERTEX
	public static final ShaderType FRAGMENT = ShaderType.fragment();
	public static final ShaderType VERTEX = ShaderType.vertex();
	//Uniforms
	public static final UniformType UN_FLOAT = UniformType.unFloat();
	public static final UniformType VEC3 = UniformType.vec3();
	//Shaders
	public static final MirrorShaderProgram ALPHA = loadProgram(
			ResourceLibrary.BLEND_SHADER,
			ImmutableList.of(FRAGMENT, VERTEX),
			ImmutableMap.of("alpha", UN_FLOAT)
	);
	public static final MirrorShaderProgram BRIGHT = loadProgram(
			ResourceLibrary.BRIGHT_SHADER,
			ImmutableList.of(FRAGMENT, VERTEX),
			ImmutableMap.of("brightness", UN_FLOAT)
	);
	public static final MirrorShaderProgram RECOLOR = loadProgram(
			ResourceLibrary.RECOLOR_SHADER,
			ImmutableList.of(FRAGMENT, VERTEX),
			ImmutableMap.of("rgba", VEC3, "greybase", UN_FLOAT)
	);

	private static MirrorShaderProgram loadProgram(ResourceLocation location, List<ShaderType> shaders, Map<String, UniformType> types) {
		Map<String, UniformBase<? extends UniformType>> uniforms = Maps.newHashMap();
		types.forEach((k, v) -> uniforms.put(k, new UniformBase<>(v, 1)));
		return ShaderManager.createProgram(location, shaders, uniforms, false);
	}

	private static MirrorShaderProgram loadComplexProgram(Map<ShaderType, ResourceLocation> locations, Map<String, UniformType> types) {
		Map<String, UniformBase<? extends UniformType>> uniforms = Maps.newHashMap();
		types.forEach((k, v) -> uniforms.put(k, new UniformBase<>(v, 1)));
		return ShaderManager.createComplexProgram(locations, uniforms, false);
	}

	public static void init() {
		IPM.LOG.info("[BAKING PIE!]");
	}
}
