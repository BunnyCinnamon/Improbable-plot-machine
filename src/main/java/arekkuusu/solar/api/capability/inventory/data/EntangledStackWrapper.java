/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.inventory.data;

import arekkuusu.solar.api.helper.NBTHelper;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 * <p>
 * Default implementation for {@link ItemStack} with a quantum entangled inventory
 */
public class EntangledStackWrapper extends EntangledIItemWrapper {

	private final ItemStack stack;

	/**
	 * @param stack  The {@link ItemStack}
	 * @param slots  Slot amount
	 */
	public EntangledStackWrapper(ItemStack stack, int slots) {
		super(slots);
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(NBTHelper.getUniqueID(stack, IEntangledIItemHandler.NBT_TAG));
	}

	@Override
	public void setKey(UUID key) {
		NBTHelper.setUniqueID(stack, IEntangledIItemHandler.NBT_TAG, key);
	}
}
