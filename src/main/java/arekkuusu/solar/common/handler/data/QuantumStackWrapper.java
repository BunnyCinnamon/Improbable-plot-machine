/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.api.entanglement.quantum.IQuantumStack;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public class QuantumStackWrapper extends QuantumDataHandler {

	private final IQuantumStack quantum;
	private final ItemStack stack;

	public QuantumStackWrapper(IQuantumStack quantum, ItemStack stack, int size) {
		super(size);
		this.quantum = quantum;
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		return quantum.getKey(stack);
	}
}
