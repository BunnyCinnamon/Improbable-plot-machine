/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import arekkuusu.solar.common.lib.LibMod;
import net.katsstuff.mirror.client.baked.Baked;
import net.katsstuff.mirror.client.helper.ResourceHelperStatic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by <Arekkuusu> on 29/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class DummyBakedRegistry {

	private static final Map<ResourceLocation, Supplier<Baked>> BAKERS = new HashMap<>();

	public static void register(Item item, Supplier<Baked> baked) {
		ResourceLocation location = item.getRegistryName();
		BAKERS.putIfAbsent(location, baked);
	}

	public static void register(Block block, Supplier<Baked> baked) {
		register(Item.getItemFromBlock(block), baked);
	}

	public static void register(String name, Supplier<Baked> baked) {
		BAKERS.put(ResourceHelperStatic.getSimple(LibMod.MOD_ID, name), baked);
	}

	public static Baked getBaked(ResourceLocation location, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> function) {
		return BAKERS.get(location).get().applyFormat(format).applyTextures(function);
	}

	public static boolean isRegistered(ResourceLocation location) {
		return BAKERS.containsKey(location);
	}
}
