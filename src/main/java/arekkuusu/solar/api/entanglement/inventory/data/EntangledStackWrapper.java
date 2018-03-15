/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.inventory.data;

import arekkuusu.solar.api.entanglement.inventory.IEntangledIItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public class EntangledStackWrapper<T extends Item & IEntangledIItemStack> extends EntangledIItemWrapper {

	private final T quantum;
	private final ItemStack stack;

	public EntangledStackWrapper(T quantum, ItemStack stack, int size) {
		super(size);
		this.quantum = quantum;
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		return quantum.getKey(stack);
	}
}
