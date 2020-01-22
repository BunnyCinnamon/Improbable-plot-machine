package arekkuusu.implom.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class MeltingRecipe {

	public final RecipeMatch<ItemStack> recipeMatch;
	public final FluidStack fluid;
	public final int temperature;

	public MeltingRecipe(RecipeMatch<ItemStack> recipeMatch, FluidStack fluid, int temperature) {
		this.recipeMatch = recipeMatch;
		this.fluid = fluid;
		this.temperature = temperature;
	}

	public boolean isMatch(ItemStack stack) {
		return recipeMatch.match(NonNullList.withSize(1, stack)).isPresent();
	}
}
