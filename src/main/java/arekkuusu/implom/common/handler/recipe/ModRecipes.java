package arekkuusu.implom.common.handler.recipe;

import arekkuusu.implom.IPMConfig;
import arekkuusu.implom.api.API;
import arekkuusu.implom.api.recipe.EvaporationRecipe;
import arekkuusu.implom.api.recipe.FuelRecipe;
import arekkuusu.implom.api.recipe.MeltingRecipe;
import arekkuusu.implom.api.recipe.RecipeMatch;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
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
        API.addFuel(new FuelRecipe(RecipeMatch.of(
                new ItemStack(Items.COAL, 1)), 50
        ));
        API.addMelting(new MeltingRecipe(RecipeMatch.of(new ResourceLocation("stone"), 1),
                new FluidStack(Fluids.LAVA, INGOT_SMELT / 2), ORE_SMELT
        ));
        API.addMelting(new MeltingRecipe(RecipeMatch.of(new ResourceLocation("cobblestone"), 1),
                new FluidStack(Fluids.LAVA, INGOT_SMELT / 2), ORE_SMELT
        ));
        API.addVapor(new EvaporationRecipe(RecipeMatch.of(Fluids.LAVA.getFluid(), INGOT_SMELT),
                (Fluids.LAVA.getAttributes().getTemperature() + 300),
                new FluidStack(Fluids.LAVA, INGOT_SMELT),
                null
        ));
    }
}
