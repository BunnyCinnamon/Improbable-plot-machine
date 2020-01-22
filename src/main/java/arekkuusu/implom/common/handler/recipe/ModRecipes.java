/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.recipe;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.api.recipe.EvaporationRecipe;
import arekkuusu.implom.api.recipe.FuelRecipe;
import arekkuusu.implom.api.recipe.MeltingRecipe;
import arekkuusu.implom.api.recipe.RecipeMatch;
import arekkuusu.implom.common.handler.ConfigHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by <Arekkuusu> on 16/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class ModRecipes {

	public static final int INGOT_SMELT = ConfigHandler.MATERIAL_CONFIG.smelting.ingotAmount;
	public static final int FRAGMENT_SMELT = INGOT_SMELT / 4;
	public static final int NUGGET_SMELT = INGOT_SMELT / 9;
	public static final int SHARD_SMELT = INGOT_SMELT / 2;
	public static final int BLOCK_SMELT = INGOT_SMELT * 9;
	public static final int BRICK_SMELT = INGOT_SMELT * 4;
	public static final int ORE_SMELT = (int) (INGOT_SMELT * ConfigHandler.MATERIAL_CONFIG.smelting.oreMultiplier);

	public static void register(IForgeRegistry<IRecipe> registry) {
		registry.register(new BoundPhotonCopyRecipe());
		registry.register(new ClockworkAddQuartzRecipe());
		registry.register(new ClockworkRemoveQuartzRecipe());
	}

	public static void initAPI() {
		IPMApi api = IPMApi.getInstance();

		api.addFuel(new FuelRecipe(RecipeMatch.of(
				new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE)), 50
		));
		api.addMelting(new MeltingRecipe(RecipeMatch.of("stone", 1),
				new FluidStack(FluidRegistry.LAVA, INGOT_SMELT / 2), ORE_SMELT
		));
		api.addMelting(new MeltingRecipe(RecipeMatch.of("cobblestone", 1),
				new FluidStack(FluidRegistry.LAVA, INGOT_SMELT / 2), ORE_SMELT
		));
		api.addVapor(new EvaporationRecipe(RecipeMatch.of(FluidRegistry.LAVA, INGOT_SMELT),
				(FluidRegistry.LAVA.getTemperature() + 300),
				new FluidStack(FluidRegistry.LAVA, INGOT_SMELT),
				null
		));
	}
}
