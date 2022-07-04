package cinnamon.implom.api.recipe;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class RecipeMatch<T> {

    public abstract Optional<Match> match(NonNullList<T> t);

    public abstract List<T> get();

    public static Stack of(ItemStack template) {
        return new Stack(template);
    }

    public static Stacks of(ItemStack... templates) {
        return new Stacks(Arrays.asList(templates));
    }

    public static StackTag of(TagKey<Item> template, int amount) {
        return new StackTag(template, amount);
    }

    public static Liquid of(Fluid template, int amount) {
        return new Liquid(new FluidStack(template, amount));
    }

    public static class Stack extends RecipeMatch<ItemStack> {

        public final ItemStack template;

        public Stack(ItemStack template) {
            this.template = template;
        }

        @Override
        public Optional<Match> match(NonNullList<ItemStack> stacks) {
            int matches = 0;
            for (ItemStack stack : stacks) {
                int needed = template.getCount();
                if (ItemStack.isSame(template, stack)) {
                    matches += stack.getCount() / needed;
                }
            }

            if (matches > 0) {
                return Optional.of(new Match(matches));
            }
            return Optional.empty();
        }

        @Override
        public List<ItemStack> get() {
            return Lists.newArrayList(template);
        }
    }

    public static class Stacks extends RecipeMatch<ItemStack> {

        public final List<ItemStack> templates;

        public Stacks(List<ItemStack> templates) {
            this.templates = templates;
        }

        @Override
        public Optional<Match> match(NonNullList<ItemStack> stacks) {
            int[] matches = new int[templates.size()];
            Arrays.fill(matches, 0);
            for (int i = 0; i < matches.length; i++) {
                ItemStack template = templates.get(i);
                for (ItemStack stack : stacks) {
                    int needed = template.getCount();
                    if (ItemStack.isSame(template, stack)) {
                        matches[i] += stack.getCount() / needed;
                    }
                }
            }

            int minMatches = Arrays.stream(matches).min().orElse(0);
            if (minMatches > 0) {
                return Optional.of(new Match(minMatches));
            }
            return Optional.empty();
        }

        @Override
        public List<ItemStack> get() {
            return Lists.newArrayList(templates);
        }
    }

    public static class StackTag extends RecipeMatch<ItemStack> {

        public final TagKey<Item> template;
        public final int amount;

        public StackTag(TagKey<Item> template, int amount) {
            this.template = template;
            this.amount = amount;
        }

        @Override
        public Optional<Match> match(NonNullList<ItemStack> stacks) {
            int matches = 0;
            for (ItemStack stack : stacks) {
                if (!stack.isEmpty() && stack.is(template)) {
                    matches += stack.getCount() / amount;
                }
            }

            if (matches > 0) {
                return Optional.of(new Match(matches));
            }
            return Optional.empty();
        }

        @Override
        public List<ItemStack> get() {
            return Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(template).stream().map(ItemStack::new).collect(Collectors.toList());
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
            for (FluidStack fluid : fluids) {
                int needed = template.getAmount();
                if (template.isFluidEqual(fluid)) {
                    matches += fluid.getAmount() / needed;
                }
            }

            if (matches > 0) {
                return Optional.of(new Match(matches));
            }
            return Optional.empty();
        }

        @Override
        public List<FluidStack> get() {
            return Lists.newArrayList(template);
        }
    }

    public static class Match {

        public final int matches;

        public Match(int matches) {
            this.matches = matches;
        }
    }
}
