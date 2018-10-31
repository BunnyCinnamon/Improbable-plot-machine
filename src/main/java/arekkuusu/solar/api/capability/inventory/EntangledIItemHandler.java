/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.inventory;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.capability.quantum.QuantumDataHandler;
import arekkuusu.solar.api.capability.quantum.data.QuantumStackData;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public final class EntangledIItemHandler {

	/**
	 * Check if the {@param uuid} is entangled to any group.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return If the key is entangled.
	 */
	public static boolean isEntangled(UUID uuid) {
		return QuantumDataHandler.get(QuantumStackData.class, uuid).isPresent();
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
		QuantumStackData data = getEntanglement(uuid);
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
	 * Gets the {@link QuantumStackData} from the given {@param uuid}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return {@link ArrayList}.
	 */
	public static QuantumStackData getEntanglement(UUID uuid) {
		return QuantumDataHandler.getOrCreate(QuantumStackData.class, uuid);
	}

	/**
	 * Map containing all {@link QuantumStackData} linked to an uuid.
	 *
	 * @return {@link HashMap}
	 */
	public static Map<UUID, QuantumStackData> getEntanglements() {
		Map<UUID, QuantumStackData> map = Maps.newHashMap();
		SolarApi.getWorldData().saved.forEach((k, v) -> {
			if(v instanceof QuantumStackData) map.put(k, (QuantumStackData) v);
		});
		return map;
	}
}
