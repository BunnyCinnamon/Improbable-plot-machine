/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.handler.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by <Arekkuusu> on 16/08/2017.
 * It's distributed as part of Solar.
 */
public final class ModRecipes {

	public static void register(IForgeRegistry<IRecipe> registry) {
		registry.register(new BoundPhotonCopyRecipe());
	}
}
