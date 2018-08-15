/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.inventory.data;

import arekkuusu.solar.api.entanglement.inventory.IEntangledIItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 *
 * Default implementation for {@link ItemStack} with a quantum entangled inventory
 */
public class EntangledStackWrapper<T extends Item & IEntangledIItemStack> extends EntangledIItemWrapper {

	private final T holder;
	private final ItemStack stack;

	/**
	 * @param holder An {@link Item} class implementing {@link T}
	 * @param stack  The {@link ItemStack}
	 * @param slots  Slot amount
	 */
	public EntangledStackWrapper(T holder, ItemStack stack, int slots) {
		super(slots);
		this.holder = holder;
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		return holder.getKey(stack);
	}
}
