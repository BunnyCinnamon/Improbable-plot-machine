/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.quantum.IEntangledStack;
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
public class QuantumStackProvider implements ICapabilityProvider {

	private final QuantumHandler handler;

	public QuantumStackProvider(QuantumHandler handler) {
		this.handler = handler;
	}

	public QuantumStackProvider(IEntangledStack quantum, ItemStack stack) {
		handler = new QuantumStackWrapper(quantum, stack);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: null;
	}
}
