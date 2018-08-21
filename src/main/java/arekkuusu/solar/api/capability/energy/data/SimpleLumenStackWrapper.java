/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy.data;

import arekkuusu.solar.api.helper.NBTHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 * <p>
 * Default implementation for {@link ItemStack} with a quantum entangled lumen storage
 */
public class SimpleLumenStackWrapper extends SimpleLumenWrapper {

	private final ItemStack stack;

	/**
	 * @param stack The {@link ItemStack}
	 * @param max   Lumen capacity
	 */
	public SimpleLumenStackWrapper(ItemStack stack, int max) {
		super(max);
		this.stack = stack;
	}

	@Override
	public int get() {
		return NBTHelper.getInteger(stack, ILumen.NBT_TAG);
	}

	@Override
	public void set(int neutrons) {
		if(neutrons <= getMax()) {
			NBTHelper.setInteger(stack, ILumen.NBT_TAG, neutrons);
		}
	}

	public ItemStack getStack() {
		return stack;
	}
}
