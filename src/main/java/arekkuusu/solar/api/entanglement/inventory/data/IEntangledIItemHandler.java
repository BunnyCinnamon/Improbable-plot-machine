/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.inventory.data;

import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public interface IEntangledIItemHandler extends IItemHandlerModifiable {

	/**
	 * Gets the {@link UUID} of the quantum inventory if it exists
	 *
	 * @return An {@link Optional<UUID>} containing the key
	 */
	Optional<UUID> getKey();
}
