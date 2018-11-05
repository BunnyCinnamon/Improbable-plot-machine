/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.baker;

import arekkuusu.implom.common.lib.LibMod;
import net.katsstuff.teamnightclipse.mirror.client.helper.ResourceHelperStatic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by <Arekkuusu> on 29/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class DummyModelRegistry {

	private static final Map<ResourceLocation, IModel> MODELS = new HashMap<>();

	public static void register(Item item, IModel model) {
		ResourceLocation location = item.getRegistryName();
		MODELS.putIfAbsent(location, model);
	}

	public static void register(Block block, IModel model) {
		register(Item.getItemFromBlock(block), model);
	}

	public static void register(String name, IModel model) {
		MODELS.put(ResourceHelperStatic.getSimple(LibMod.MOD_ID, name), model);
	}

	public static IModel getModel(ResourceLocation location) {
		return MODELS.get(location);
	}

	public static boolean isRegistered(ResourceLocation location) {
		return MODELS.containsKey(location);
	}
}
