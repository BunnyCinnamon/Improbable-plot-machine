/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 29/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class DummyBakedRegistry {

	private static final Map<ResourceLocation, BiFunction<VertexFormat, Function<ResourceLocation, TextureAtlasSprite>, IBakedModel>> BAKERS = new HashMap<>();

	public static void register(Item item, BiFunction<VertexFormat, Function<ResourceLocation, TextureAtlasSprite>, IBakedModel> function) {
		ResourceLocation location = item.getRegistryName();
		BAKERS.putIfAbsent(location, function);
	}

	public static IBakedModel getBaked(ResourceLocation location, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> function) {
		return BAKERS.get(location).apply(format, function);
	}

	public static boolean isRegistered(ResourceLocation location) {
		return BAKERS.containsKey(location);
	}
}
