/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.inventory;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.quantum.QuantumDataHandler;
import arekkuusu.solar.api.entanglement.quantum.data.QuantumIItemData;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public final class EntangledIItemHandler {

	public static final String NBT_TAG = "quantum_data";

	/**
	 * Check if the {@param uuid} is entangled to any group.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return If the key is entangled.
	 */
	public static boolean isEntangled(UUID uuid) {
		return QuantumDataHandler.get(uuid).isPresent();
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
		if(hasSlot(uuid, slot)) {
			return getEntanglement(uuid).stacks.get(slot).copy();
		}
		return ItemStack.EMPTY;
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
		QuantumIItemData data = getEntanglement(uuid);
		List<ItemStack> list = data.stacks;
		if(!hasSlot(uuid, slot)) {
			if(!stack.isEmpty()) {
				list.add(stack);
			}
		} else if(!stack.isEmpty()) {
			list.set(slot, stack);
		} else {
			list.remove(slot);
		}
		data.dirty();
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
			setEntanglementStack(uuid, stack, -1);
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
			setEntanglementStack(uuid, ItemStack.EMPTY, slot);
		}
	}

	/**
	 * If the specified {@param slot} exists.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param slot Position in the list.
	 */
	public static boolean hasSlot(UUID uuid, int slot) {
		List<ItemStack> stacks = getEntanglement(uuid).stacks;
		return slot < 0 || stacks.size() - 1 >= slot;
	}

	/**
	 * Gets the item group from the given {@param uuid}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return {@link ArrayList}.
	 */
	public static QuantumIItemData getEntanglement(UUID uuid) {
		return QuantumDataHandler.getOrCreate(uuid, QuantumIItemData::new);
	}

	/**
	 * Map containing all Items linked to an uuid.
	 *
	 * @return {@link HashMap}
	 */
	public static Map<UUID, QuantumIItemData> getEntanglements() {
		Map<UUID, QuantumIItemData> map = Maps.newHashMap();
		SolarApi.getDataMap().forEach((k, v) -> {
			if(v instanceof QuantumIItemData) map.put(k, (QuantumIItemData) v);
		});
		return map;
	}
}
