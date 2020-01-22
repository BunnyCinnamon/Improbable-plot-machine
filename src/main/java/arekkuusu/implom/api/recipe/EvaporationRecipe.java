package arekkuusu.implom.api.recipe;

import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class EvaporationRecipe {

	public final RecipeMatch.Liquid recipeMatch;
	public final int temperature;
	public final FluidStack drain;
	public final FluidStack fill;

	public EvaporationRecipe(RecipeMatch.Liquid recipeMatch, int temperature, FluidStack drain, @Nullable FluidStack fill) {
		this.recipeMatch = recipeMatch;
		this.temperature = temperature;
		this.drain = drain;
		this.fill = fill;
	}

	public boolean isMatch(FluidStack stack) {
		return recipeMatch.match(NonNullList.withSize(1, stack)).isPresent();
	}

	public int getTemperature(FluidStack stack) {
		return recipeMatch.template.getFluid().getTemperature(stack);
	}
}
