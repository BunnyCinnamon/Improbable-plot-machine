/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api;

import arekkuusu.implom.api.capability.data.INBTData;
import arekkuusu.implom.api.recipe.AlloyRecipe;
import arekkuusu.implom.api.recipe.EvaporationRecipe;
import arekkuusu.implom.api.recipe.FuelRecipe;
import arekkuusu.implom.api.recipe.MeltingRecipe;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
//If you modify any of these I will break your bones
public abstract class IPMApi {

	private static IPMApi instance;
	public final Map<UUID, Map<Class<?>, INBTData<?>>> dataMap = Maps.newHashMap();
	public final Map<ResourceLocation, Class<INBTData<?>>> classMap = Maps.newHashMap();
	public final List<FuelRecipe> fuelRecipes = Lists.newLinkedList();
	public final List<AlloyRecipe> alloyRecipes = Lists.newLinkedList();
	public final List<MeltingRecipe> meltingRecipes = Lists.newLinkedList();
	public final List<EvaporationRecipe> evaporationRecipes = Lists.newLinkedList();

	public static void setInstance(IPMApi instance) {
		if(IPMApi.instance == null) {
			IPMApi.instance = instance;
		}
	}

	public static IPMApi getInstance() {
		return instance;
	}

	public abstract void loadWorld(World world);

	public abstract void unloadWorld();

	public abstract void markWorldDirty();

	public void addFuel(FuelRecipe recipe) {
		fuelRecipes.add(recipe);
	}

	public void addAlloy(AlloyRecipe recipe) {
		alloyRecipes.add(recipe);
	}

	public void addMelting(MeltingRecipe recipe) {
		meltingRecipes.add(recipe);
	}

	public void addVapor(EvaporationRecipe recipe) {
		evaporationRecipes.add(recipe);
	}
}
