package cinnamon.implom.common.handler.recipe;

import cinnamon.implom.IPMConfig;
import cinnamon.implom.api.API;
import cinnamon.implom.api.recipe.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    public static final int ORE_TEMPERATURE = 50;
    public static final int ICE_TEMPERATURE = 50;

    public static void init() {
        API.fuelRecipes.clear();
        API.alloyRecipes.clear();
        API.evaporationRecipes.clear();
        API.meltingRecipes.clear();
        API.addFuel(new FuelRecipe(RecipeMatch.of(
                ItemTags.COALS, 1), 50
        ));
        API.addMelting(new MeltingRecipe(RecipeMatch.of(Tags.Items.STONE, 1),
                new FluidStack(Fluids.LAVA, INGOT_SMELT / 2), ORE_TEMPERATURE
        ));
        API.addMelting(new MeltingRecipe(RecipeMatch.of(Tags.Items.COBBLESTONE, 1),
                new FluidStack(Fluids.LAVA, INGOT_SMELT / 2), ORE_TEMPERATURE
        ));
        API.addMelting(new MeltingRecipe(RecipeMatch.of(new ItemStack(Items.PACKED_ICE, 1)),
                new FluidStack(Fluids.WATER, INGOT_SMELT / 2), ICE_TEMPERATURE
        ));
        API.addAlloy(new AlloyRecipe(FluidStack.EMPTY,
                new FluidStack(Fluids.LAVA, INGOT_SMELT / 2),
                new FluidStack(Fluids.WATER, INGOT_SMELT / 2)
        ));
        /*API.addVapor(new EvaporationRecipe(RecipeMatch.of(Fluids.LAVA.getSource(), INGOT_SMELT),
                (Fluids.LAVA.getAttributes().getTemperature() + 300),
                new FluidStack(Fluids.LAVA, INGOT_SMELT),
                FluidStack.EMPTY
        ));*/
    }
}
