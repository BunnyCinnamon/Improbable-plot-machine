/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.util.baker;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class EmptyRenderRegistry {

	private static final Map<ResourceLocation, IModelRenderer> RENDERS = new HashMap<>();

	public static void register(Item item, IModelRenderer renderer) {
		ResourceLocation location = item.getRegistryName();

		ModelLoader.setCustomMeshDefinition(item, new DummyMeshDefinition(location));

		RENDERS.putIfAbsent(location, renderer);
	}

	public static boolean isRegistered(ResourceLocation location) {
		return RENDERS.containsKey(location);
	}

	private static class DummyMeshDefinition implements ItemMeshDefinition {

		private ModelResourceLocation fallback;

		public DummyMeshDefinition(ResourceLocation loc) {
			this.fallback = new ModelResourceLocation(loc, "inventory");
		}

		@Override
		@Nonnull
		public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
			return fallback;
		}
	}
}
