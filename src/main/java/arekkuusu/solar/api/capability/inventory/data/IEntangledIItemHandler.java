/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.inventory.data;

import arekkuusu.solar.api.capability.quantum.IQuantum;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public interface IEntangledIItemHandler extends IItemHandlerModifiable, IQuantum {
	String NBT_TAG = "quantum_stack_nbt";
}
