/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api;

import arekkuusu.solar.api.entanglement.relativity.IRelativeTile;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class was created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
public class SolarApi {

	private static final Map<UUID, List<ItemStack>> ENTANGLED_STACKS = new HashMap<>();
	private static final Map<UUID, List<IRelativeTile>> RELATIVITY_MAP = new HashMap<>();
	public static final String QUANTUM_DATA = "quantum_data";

	/**
	 * Map containing all Items linked to an uuid.
	 * <p>
	 *     This is most likely empty client side,
	 *     use {@code QuantumHandler.getSidedMap()} instead.
	 *
	 *     Do not modify unless you sync it yourself.
	 * </p>
	 *
	 * @return {@link HashMap}
	 */
	public static Map<UUID, List<ItemStack>> getEntangledStacks() {
		return ENTANGLED_STACKS;
	}

	public static Map<UUID, List<IRelativeTile>> getRelativityMap() {
		return RELATIVITY_MAP;
	}
}
