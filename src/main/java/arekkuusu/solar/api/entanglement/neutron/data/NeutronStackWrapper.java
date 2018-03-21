/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.neutron.data;

import arekkuusu.solar.api.entanglement.inventory.IEntangledIItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public class NeutronStackWrapper<T extends Item & IEntangledIItemStack> extends NeutronWrapper {

	private final T holder;
	private final ItemStack stack;

	public NeutronStackWrapper(T holder, ItemStack stack, int max) {
		super(max);
		this.holder = holder;
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		return holder.getKey(stack);
	}
}
