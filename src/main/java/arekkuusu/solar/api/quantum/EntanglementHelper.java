/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.api.quantum;

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
public class EntanglementHelper {

	private static final Map<UUID, List<ItemStack>> TEMP_STACKS = new HashMap<>();

	public static boolean isEntangled(UUID uuid) {
		return getSidedMap().containsKey(uuid);
	}

	public static ItemStack getQuantumStack(UUID uuid, int slot) {
		Map<UUID, List<ItemStack>> map = getSidedMap();
		if(map.containsKey(uuid) && hasSlot(uuid, slot)) {
			return map.get(uuid).get(slot);
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

	public static void removeQuantumAsync(UUID uuid, int slot) {
		if(hasSlot(uuid, slot)) {
			getQuantumStacks(uuid).remove(slot);
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
		return getSidedMap().computeIfAbsent(uuid, u -> new ArrayList<>());
	}

	public static Map<UUID, List<ItemStack>> getSidedMap() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		return side == Side.SERVER ? SolarApi.getStacks() : TEMP_STACKS;
	}

	private static boolean hasSlot(UUID uuid, int slot) {
		List<ItemStack> stacks = getQuantumStacks(uuid);
		return slot < 0 || stacks.size() - 1 >= slot;
	}
}
