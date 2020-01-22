package arekkuusu.implom.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Optional;

public class FuelRecipe {

	public final RecipeMatch<ItemStack> recipeMatch;
	public final int heat;

	public FuelRecipe(RecipeMatch<ItemStack> recipeMatch, int heat) {
		this.recipeMatch = recipeMatch;
		this.heat = heat;
	}

	public Optional<RecipeMatch.Match> match(ItemStack stack) {
		return recipeMatch.match(NonNullList.withSize(1, stack));
	}
}
