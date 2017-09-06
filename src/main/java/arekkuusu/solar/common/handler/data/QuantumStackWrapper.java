/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.entanglement.quantum.IQuantumStack;
import arekkuusu.solar.api.entanglement.quantum.QuantumHandler;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public class QuantumStackWrapper extends arekkuusu.solar.common.handler.data.QuantumHandler {

	private final IQuantumStack quantum;
	private final ItemStack stack;

	public QuantumStackWrapper(IQuantumStack quantum, ItemStack stack) {
		super(quantum.getSlots());
		this.quantum = quantum;
		this.stack = stack;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack inserted) {
		QuantumHandler.setQuantumStack(getKey(), inserted, slot);
	}

	@Nullable
	@Override
	public UUID getKey() {
		return quantum.getKey(stack).orElse(null);
	}
}
