package arekkuusu.implom.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class RecipeMatch<T> {

	public abstract Optional<Match> match(NonNullList<T> t);

	public static Item of(ItemStack template) {
		return new Item(template);
	}

	public static Items of(ItemStack... templates) {
		return new Items(Arrays.asList(templates));
	}

	public static OreDic of(String template, int amount) {
		return new OreDic(template, amount);
	}

	public static Liquid of(Fluid template, int amount) {
		return new Liquid(new FluidStack(template, amount));
	}

	public static class Item extends RecipeMatch<ItemStack> {

		public final ItemStack template;

		public Item(ItemStack template) {
			this.template = template;
		}

		@Override
		public Optional<Match> match(NonNullList<ItemStack> stacks) {
			int matches = 0;
			for(ItemStack stack : stacks) {
				int needed = template.getCount();
				if(OreDictionary.itemMatches(template, stack, false)) {
					matches += stack.getCount() / needed;
				}
			}

			if(matches > 0) {
				return Optional.of(new Match(matches));
			}
			return Optional.empty();
		}
	}

	public static class Items extends RecipeMatch<ItemStack> {

		public final List<ItemStack> templates;

		public Items(List<ItemStack> templates) {
			this.templates = templates;
		}

		@Override
		public Optional<Match> match(NonNullList<ItemStack> stacks) {
			int[] matches = new int[templates.size()];
			Arrays.fill(matches, 0);
			for(int i = 0; i < matches.length; i++) {
				ItemStack template = templates.get(i);
				for(ItemStack stack : stacks) {
					int needed = template.getCount();
					if(OreDictionary.itemMatches(template, stack, false)) {
						matches[i] += stack.getCount() / needed;
					}
				}
			}

			int minMatches = Arrays.stream(matches).min().orElse(0);
			if(minMatches > 0) {
				return Optional.of(new Match(minMatches));
			}
			return Optional.empty();
		}
	}

	public static class OreDic extends RecipeMatch<ItemStack> {

		public final List<ItemStack> templates;
		public final int amount;

		public OreDic(String template, int amount) {
			this.templates = OreDictionary.getOres(template);
			this.amount = amount;
		}

		@Override
		public Optional<Match> match(NonNullList<ItemStack> stacks) {
			int matches = 0;
			for(ItemStack template : templates) {
				for(ItemStack stack : stacks) {
					if(OreDictionary.itemMatches(template, stack, false)) {
						matches += stack.getCount() / amount;
					}
				}
			}

			if(matches > 0) {
				return Optional.of(new Match(matches));
			}
			return Optional.empty();
		}
	}

	public static class Liquid extends RecipeMatch<FluidStack> {

		public final FluidStack template;

		public Liquid(FluidStack template) {
			this.template = template;
		}

		@Override
		public Optional<Match> match(NonNullList<FluidStack> fluids) {
			int matches = 0;
			for(FluidStack fluid : fluids) {
				int needed = template.amount;
				if(template.isFluidEqual(fluid)) {
					matches += fluid.amount / needed;
				}
			}

			if(matches > 0) {
				return Optional.of(new Match(matches));
			}
			return Optional.empty();
		}
	}

	public static class Match {

		public final int matches;

		public Match(int matches) {
			this.matches = matches;
		}
	}
}
