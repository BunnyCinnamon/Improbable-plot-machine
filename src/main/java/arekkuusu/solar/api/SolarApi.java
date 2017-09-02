/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.api;

import arekkuusu.solar.common.handler.data.WorldQuantumData;
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

	public static ItemStack getQuantumStack(UUID uuid, int slot) {
		if(QUANTUM_ITEMS.containsKey(uuid) && hasSlot(uuid, slot)) {
			return QUANTUM_ITEMS.get(uuid).get(slot);
		}
		return ItemStack.EMPTY;
	}

	public static void setQuantumAsync(UUID uuid, ItemStack stack, int slot) {
		List<ItemStack> list = getQuantumStacks(uuid);
		if(!hasSlot(uuid, slot)) {
			if(!stack.isEmpty()) {
				list.add(stack);
			}
		} else if(!stack.isEmpty()) {
			list.set(slot, stack);
		} else {
			list.remove(slot);
		}
	}

	public static void addQuantumAsync(UUID uuid, ItemStack stack){
		if(!stack.isEmpty()) {
			getQuantumStacks(uuid).add(stack);
		}
	}

	public static void setQuantumStack(UUID uuid, ItemStack stack, int slot) {
		WorldQuantumData.syncChange(uuid, stack, slot);
		setQuantumAsync(uuid, stack, slot);
	}

	public static void addQuantumStack(UUID uuid, ItemStack stack) {
		if(!stack.isEmpty()) {
			setQuantumStack(uuid, stack, -1);
		}
	}

	public static void removeQuantumStack(UUID uuid, int slot) {
		if(hasSlot(uuid, slot)) {
			setQuantumStack(uuid, ItemStack.EMPTY, slot);
		}
	}

	public static List<ItemStack> getQuantumStacks(UUID uuid) {
		return QUANTUM_ITEMS.computeIfAbsent(uuid, u -> new ArrayList<>());
	}

	private static boolean hasSlot(UUID uuid, int slot) {
		List<ItemStack> stacks = getQuantumStacks(uuid);
		return slot < 0 || stacks.size() - 1 >= slot;
	}
}
