package arekkuusu.implom.api.recipe;

import com.google.common.collect.Lists;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class RecipeMatch<T> {

    public abstract Optional<Match> match(NonNullList<T> t);

    public abstract List<T> get();

    public static Item of(ItemStack template) {
        return new Item(template);
    }

    public static Items of(ItemStack... templates) {
        return new Items(Arrays.asList(templates));
    }

    public static OreDic of(ResourceLocation template, int amount) {
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
            for (ItemStack stack : stacks) {
                int needed = template.getCount();
                if (ItemStack.areItemStacksEqual(template, stack)) {
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

    public static class Items extends RecipeMatch<ItemStack> {

        public final List<ItemStack> templates;

        public Items(List<ItemStack> templates) {
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
                    if (ItemStack.areItemStacksEqual(template, stack)) {
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

    public static class OreDic extends RecipeMatch<ItemStack> {

        public final ITag<net.minecraft.item.Item> template;
        public final int amount;

        public OreDic(ResourceLocation template, int amount) {
            this.template = ItemTags.getCollection().getOrCreate(template);
            this.amount = amount;
        }

        @Override
        public Optional<Match> match(NonNullList<ItemStack> stacks) {
            int matches = 0;
            for (ItemStack stack : stacks) {
                if (!stack.isEmpty() && template.contains(stack.getItem())) {
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
            return template.getAllElements().stream().map(ItemStack::new).collect(Collectors.toList());
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
