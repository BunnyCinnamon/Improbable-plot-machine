package arekkuusu.implom.api.recipe;

import arekkuusu.implom.api.util.IPMAPIException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.ListIterator;

public class AlloyRecipe {

	public final List<FluidStack> fluids;
	public final FluidStack result;

	public AlloyRecipe(FluidStack result, FluidStack... recipe) {
		this.result = result;

		ImmutableList.Builder<FluidStack> builder = ImmutableList.builder();
		for(FluidStack liquid : recipe) {
			if(liquid == null) {
				throw new IPMAPIException("Invalid Alloy recipe: Input cannot be null");
			}
			if(liquid.amount < 1) {
				throw new IPMAPIException("Invalid Alloy recipe: Fluid amount can't be less than 1");
			}
			if(liquid.containsFluid(result)) {
				throw new IPMAPIException("Invalid Alloy recipe: Result cannot be contained in inputs");
			}
			builder.add(liquid);
		}

		fluids = builder.build();
	}

	public int matches(List<FluidStack> input) {
		// how often we can apply the alloy
		int times = Integer.MAX_VALUE;
		List<FluidStack> needed = Lists.newLinkedList(fluids);
		// for each liquid in the input
		for(FluidStack fluid : input) {
			// check if it's needed
			ListIterator<FluidStack> iter = needed.listIterator();
			while(iter.hasNext()) {
				FluidStack need = iter.next();
				if(fluid.containsFluid(need)) {
					// and if it matches, remove
					iter.remove();

					// check how often we can apply the recipe with this
					if(fluid.amount / need.amount < times) {
						times = fluid.amount / need.amount;
					}
					break;
				}
			}
		}
		// if the needed array is empty we found everything we needed
		return needed.isEmpty() ? times : 0;
	}
}
