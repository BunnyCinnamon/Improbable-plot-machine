/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum.data;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 19/12/2017.
 * It's distributed as part of Solar.
 */
public interface IQuantumData {

	default boolean isEntangled(UUID uuid) {
		return getEntanglements().containsKey(uuid);
	}

	default ItemStack getEntanglementStack(UUID uuid, int slot) {
		if(getEntanglements().containsKey(uuid) && hasSlot(uuid, slot)) {
			return getEntanglements().get(uuid).get(slot).copy();
		}
		return ItemStack.EMPTY;
	}

	default void setEntanglementStack(UUID uuid, ItemStack stack, int slot) {
		List<ItemStack> list = getEntanglement(uuid);
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

	default List<ItemStack> getEntanglement(UUID uuid) {
		return getEntanglements().computeIfAbsent(uuid, u -> new ArrayList<>());
	}

	default boolean hasSlot(UUID uuid, int slot) {
		List<ItemStack> stacks = getEntanglement(uuid);
		return slot < 0 || stacks.size() - 1 >= slot;
	}

	Map<UUID, List<ItemStack>> getEntanglements();
}
