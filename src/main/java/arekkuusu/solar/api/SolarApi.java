/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.api;

import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * This class was created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
public class SolarApi {

	public static final Map<UUID, List<ItemStack>> QUANTUM_ITEMS = new HashMap<>();
	public static final String QUANTUM_DATA = "quantum_data";

	public static boolean isQuantumEntangled(UUID uuid) {
		return QUANTUM_ITEMS.containsKey(uuid);
	}

	public static ItemStack getQuantumItem(UUID uuid, int slot) {
		if(QUANTUM_ITEMS.containsKey(uuid) && hasSlot(uuid, slot)) {
			return QUANTUM_ITEMS.get(uuid).get(slot);
		}
		return ItemStack.EMPTY;
	}

	public static void setQuantumItem(UUID uuid, ItemStack stack, int slot) {
		List<ItemStack> list = getQuantumList(uuid);
		if(list.isEmpty() || list.size() <= slot) {
			if(!stack.isEmpty()) {
				list.add(stack);
			}
		} else if(!stack.isEmpty()) {
			list.set(slot, stack);
		} else {
			list.remove(slot);
		}
	}

	public static ItemStack popQuantumItem(UUID uuid) {
		List<ItemStack> list = getQuantumList(uuid);
		if(!list.isEmpty()) {
			ItemStack stack = list.get(list.size() - 1);
			list.remove(list.size() - 1);
			return stack;
		}
		return ItemStack.EMPTY;
	}

	public static void putQuantumItem(UUID uuid, ItemStack stack) {
		if(!stack.isEmpty()) {
			getQuantumList(uuid).add(stack);
		}
	}

	public static List<ItemStack> getQuantumList(UUID uuid) {
		return QUANTUM_ITEMS.computeIfAbsent(uuid, u -> new ArrayList<>(1));
	}

	public static boolean hasSlot(UUID uuid, int slot) {
		List<ItemStack> list = getQuantumList(uuid);
		return list.size() - 1 >= slot;
	}
}
