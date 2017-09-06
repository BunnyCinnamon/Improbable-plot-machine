/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("WeakerAccess")
public class QuantumHandler {

	private static final Map<UUID, List<ItemStack>> TEMP_STACKS = new HashMap<>();

	/**
	 * Check if the {@param uuid} is entangled to any group.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return If the key is entangled.
	 */
	public static boolean isEntangled(UUID uuid) {
		return getSidedMap().containsKey(uuid);
	}

	/**
	 * Get the {@link ItemStack} contained in the
	 * specified {@param slot}.
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param slot Position in the list.
	 * @return The original ItemStack.
	 */
	public static ItemStack getQuantumStack(UUID uuid, int slot) {
		Map<UUID, List<ItemStack>> map = getSidedMap();
		if(map.containsKey(uuid) && hasSlot(uuid, slot)) {
			return map.get(uuid).get(slot);
		}
		return ItemStack.EMPTY;
	}

	/**
	 * Set a {@param stack} to the given {@param uuid}
	 * in the specified {@param slot}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param stack {@link ItemStack}
	 * @param slot Position in the list.
	 */
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

	/**
	 * Add a {@param stack} to the item
	 * group of the specified {@param uuid}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param stack {@link ItemStack}
	 */
	public static void addQuantumAsync(UUID uuid, ItemStack stack){
		if(!stack.isEmpty()) {
			getQuantumStacks(uuid).add(stack);
		}
	}

	/**
	 * Remove the {@link ItemStack} contained in the
	 * specified {@param slot}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param slot Position in the list.
	 */
	public static void removeQuantumAsync(UUID uuid, int slot) {
		if(hasSlot(uuid, slot)) {
			getQuantumStacks(uuid).remove(slot);
		}
	}

	/**
	 * Set and Sync to the item group of the specified {@param uuid}
	 * <p>
	 *     See {@code QuantumHandler.setQuantumAsync()}
	 * </p>
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param stack {@link ItemStack}
	 * @param slot Position in the list.
	 */
	public static void setQuantumStack(UUID uuid, ItemStack stack, int slot) {
		WorldQuantumData.syncChange(uuid, stack, slot);
		setQuantumAsync(uuid, stack, slot);
	}

	/**
	 * Add and Sync to the item group of the specified {@param uuid}
	 * <p>
	 *     See {@code QuantumHandler.addQuantumAsync()}
	 * </p>
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param stack {@link ItemStack}
	 */
	public static void addQuantumStack(UUID uuid, ItemStack stack) {
		if(!stack.isEmpty()) {
			setQuantumStack(uuid, stack, -1);
		}
	}

	/**
	 * Remove and Sync to the item group of the specified {@param uuid}
	 * <p>
	 *     See {@code QuantumHandler.removeQuantumAsync()}
	 * </p>
	 *
	 * @param uuid Key to the group of entangled items.
	 * @param slot Position in the list.
	 */
	public static void removeQuantumStack(UUID uuid, int slot) {
		if(hasSlot(uuid, slot)) {
			setQuantumStack(uuid, ItemStack.EMPTY, slot);
		}
	}

	/**
	 * Gets the item group from the given {@param uuid}
	 *
	 * @param uuid Key to the group of entangled items.
	 * @return {@link ArrayList}.
	 */
	public static List<ItemStack> getQuantumStacks(UUID uuid) {
		return getSidedMap().computeIfAbsent(uuid, u -> new ArrayList<>());
	}

	/**
	 * Sided sensitive version of {@code SolarApi.getStacks()}
	 * <p>
	 *     Depending on the {@link Side}
	 *     it returns a different map.
	 * </p>
	 *
	 * @return {@link HashMap}
	 */
	public static Map<UUID, List<ItemStack>> getSidedMap() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		return side == Side.SERVER ? SolarApi.getStacks() : TEMP_STACKS;
	}

	private static boolean hasSlot(UUID uuid, int slot) {
		List<ItemStack> stacks = getQuantumStacks(uuid);
		return slot < 0 || stacks.size() - 1 >= slot;
	}
}
