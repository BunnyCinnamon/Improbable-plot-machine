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
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 11/08/2017.
 * It's distributed as part of Solar.
 */
public class EntangledStackProvider<T extends Item & IEntangledIItemStack> implements ICapabilityProvider {

	private final EntangledIItemWrapper handler;

	public EntangledStackProvider(EntangledIItemWrapper handler) {
		this.handler = handler;
	}

	public EntangledStackProvider(T quantum, ItemStack stack, int size) {
		handler = new EntangledStackWrapper<>(quantum, stack, size);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <C> C getCapability(Capability<C> capability, @Nullable EnumFacing facing) {
		return facing == null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: null;
	}
}
