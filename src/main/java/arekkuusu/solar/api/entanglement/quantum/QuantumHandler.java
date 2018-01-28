/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.SolarApi;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public final class QuantumHandler {

	public static final String NBT_TAG = "quantum_data";

	/**
	 * Check if the {@param uuid} is entangled to any group.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return If the key is entangled.
	 */
	public static boolean isEntangled(UUID uuid) {
		return SolarApi.getQuantumData().isEntangled(uuid);
	}

	/**
	 * Get the {@link ItemStack} contained in the
	 * specified {@param slot}.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param slot Position in the list.
	 * @return The copy of ItemStack.
	 */
	public static ItemStack getEntanglementStack(UUID uuid, int slot) {
		return SolarApi.getQuantumData().getEntanglementStack(uuid, slot);
	}

	/**
	 * Set a {@param stack} to the given {@param uuid}
	 * in the specified {@param slot}
	 *
	 * @param uuid  Key to the group of entangled items.
	 * @param stack {@link ItemStack}
	 * @param slot  Position in the list.
	 */
	public static void setEntanglementStack(UUID uuid, ItemStack stack, int slot) {
		SolarApi.getQuantumData().setEntanglementStack(uuid, stack, slot);
	}

	/**
	 * Add a {@param stack} to the item
	 * group of the specified {@param uuid}
	 *
	 * @param uuid  Key to the group of entangled items.
	 * @param stack {@link ItemStack}
	 */
	public static void addEntanglementStack(UUID uuid, ItemStack stack) {
		if(!stack.isEmpty()) {
			SolarApi.getQuantumData().setEntanglementStack(uuid, stack, -1);
		}
	}

	/**
	 * Remove the {@link ItemStack} contained in the
	 * specified {@param slot}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param slot Position in the list.
	 */
	public static void removeEntanglementStack(UUID uuid, int slot) {
		if(hasSlot(uuid, slot)) {
			SolarApi.getQuantumData().setEntanglementStack(uuid, ItemStack.EMPTY, slot);
		}
	}

	/**
	 * If the specified {@param slot} exists.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param slot Position in the list.
	 */
	public static boolean hasSlot(UUID uuid, int slot) {
		return SolarApi.getQuantumData().hasSlot(uuid, slot);
	}

	/**
	 * Gets the item group from the given {@param uuid}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return {@link ArrayList}.
	 */
	public static List<ItemStack> getEntanglement(UUID uuid) {
		return SolarApi.getQuantumData().getEntanglement(uuid);
	}

	/**
	 * Map containing all Items linked to an uuid.
	 *
	 * @return {@link HashMap}
	 */
	public static Map<UUID, List<ItemStack>> getEntanglements() {
		return SolarApi.getQuantumData().getEntanglements();
	}
}
