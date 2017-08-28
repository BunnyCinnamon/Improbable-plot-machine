/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 29/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class DummyBakedRegistry {

	private static final Map<ResourceLocation, Function<Pair<VertexFormat, Function<ResourceLocation, TextureAtlasSprite>>, IBakedModel>> BAKERS = new HashMap<>();

	public static void register(Item item, Function<Pair<VertexFormat, Function<ResourceLocation, TextureAtlasSprite>>, IBakedModel> function) {
		ResourceLocation location = item.getRegistryName();

		BAKERS.putIfAbsent(location, function);
	}

	public static IBakedModel getBaked(ResourceLocation location, Pair<VertexFormat, Function<ResourceLocation, TextureAtlasSprite>> pair) {
		return BAKERS.get(location).apply(pair);
	}

	public static boolean isRegistered(ResourceLocation location) {
		return BAKERS.containsKey(location);
	}
}
