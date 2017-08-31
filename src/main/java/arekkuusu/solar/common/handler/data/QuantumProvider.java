/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.quantum.IQuantumItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 11/08/2017.
 * It's distributed as part of Solar.
 */
public class QuantumProvider implements ICapabilityProvider {

	private final QuantumHandler handler;

	public QuantumProvider(QuantumHandler handler) {
		this.handler = handler;
	}

	public QuantumProvider(IQuantumItem quantum, ItemStack stack) {
		handler = new QuantumItemHandler(quantum, stack);
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

	public static class QuantumItemHandler extends QuantumHandler {

		private final IQuantumItem quantum;
		private final ItemStack stack;

		public QuantumItemHandler(IQuantumItem quantum, ItemStack stack) {
			super(quantum.getSlots());
			this.quantum = quantum;
			this.stack = stack;
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			quantum.setQuantumStack(this.stack, stack, slot);
		}

		@Nullable
		@Override
		public UUID getKey() {
			return quantum.getKey(stack).orElse(null);
		}
	}
}
