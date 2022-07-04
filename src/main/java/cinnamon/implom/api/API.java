package cinnamon.implom.api;

import cinnamon.implom.api.recipe.AlloyRecipe;
import cinnamon.implom.api.recipe.EvaporationRecipe;
import cinnamon.implom.api.recipe.FuelRecipe;
import cinnamon.implom.api.recipe.MeltingRecipe;
import com.google.common.collect.Lists;

import java.util.List;

//I will break your ear bones
public class API {

    public static final List<FuelRecipe> fuelRecipes = Lists.newLinkedList();
    public static final List<AlloyRecipe> alloyRecipes = Lists.newLinkedList();
    public static final List<MeltingRecipe> meltingRecipes = Lists.newLinkedList();
    public static final List<EvaporationRecipe> evaporationRecipes = Lists.newLinkedList();

    public static void addFuel(FuelRecipe recipe) {
        fuelRecipes.add(recipe);
    }

    public static void addAlloy(AlloyRecipe recipe) {
        alloyRecipes.add(recipe);
    }

    public static void addMelting(MeltingRecipe recipe) {
        meltingRecipes.add(recipe);
    }

    public static void addVapor(EvaporationRecipe recipe) {
        evaporationRecipes.add(recipe);
    }
}
