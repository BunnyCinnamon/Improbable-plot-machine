/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.energy.data;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 *
 * Default implementation for {@link ItemStack} with a quantum entangled lumen storage
 */
public class LumenStackWrapper<T extends Item & IEntangledStack> extends LumenWrapper {

	private final T holder;
	private final ItemStack stack;

	/**
	 * @param holder An {@link Item} class implementing {@link T}
	 * @param stack  The {@link ItemStack}
	 * @param max    Lumen capacity
	 */
	public LumenStackWrapper(T holder, ItemStack stack, int max) {
		super(max);
		this.holder = holder;
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		return holder.getKey(stack);
	}
}
