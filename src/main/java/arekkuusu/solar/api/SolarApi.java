/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	private static final Map<UUID, List<ItemStack>> QUANTUM_STACKS = new HashMap<>();
	public static final String QUANTUM_DATA = "quantum_data";

	public static Map<UUID, List<ItemStack>> getStacks() {
		return QUANTUM_STACKS;
	}
}
