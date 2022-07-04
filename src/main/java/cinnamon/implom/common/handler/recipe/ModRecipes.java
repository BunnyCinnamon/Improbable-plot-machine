package cinnamon.implom.common.handler.recipe;

import cinnamon.implom.IPMConfig;
import cinnamon.implom.api.API;
import cinnamon.implom.api.recipe.EvaporationRecipe;
import cinnamon.implom.api.recipe.FuelRecipe;
import cinnamon.implom.api.recipe.MeltingRecipe;
import cinnamon.implom.api.recipe.RecipeMatch;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

public final class ModRecipes {

    public static final int INGOT_SMELT = IPMConfig.Runtime.Smelting.ingotAmount;
    public static final int FRAGMENT_SMELT = INGOT_SMELT / 4;
    public static final int NUGGET_SMELT = INGOT_SMELT / 9;
    public static final int SHARD_SMELT = INGOT_SMELT / 2;
    public static final int BLOCK_SMELT = INGOT_SMELT * 9;
    public static final int BRICK_SMELT = INGOT_SMELT * 4;
    public static final int ORE_SMELT = INGOT_SMELT * IPMConfig.Runtime.Smelting.oreMultiplier;

    public static void init() {
        API.fuelRecipes.clear();
        API.alloyRecipes.clear();
        API.evaporationRecipes.clear();
        API.meltingRecipes.clear();
        API.addFuel(new FuelRecipe(RecipeMatch.of(
                ItemTags.COALS, 1), 50
        ));
        API.addMelting(new MeltingRecipe(RecipeMatch.of(Tags.Items.STONE, 1),
                new FluidStack(Fluids.LAVA, INGOT_SMELT / 2), ORE_SMELT
        ));
        API.addMelting(new MeltingRecipe(RecipeMatch.of(Tags.Items.COBBLESTONE, 1),
                new FluidStack(Fluids.LAVA, INGOT_SMELT / 2), ORE_SMELT
        ));
        API.addVapor(new EvaporationRecipe(RecipeMatch.of(Fluids.LAVA.getSource(), INGOT_SMELT),
                (Fluids.LAVA.getAttributes().getTemperature() + 300),
                new FluidStack(Fluids.LAVA, INGOT_SMELT),
                FluidStack.EMPTY
        ));
    }
}
