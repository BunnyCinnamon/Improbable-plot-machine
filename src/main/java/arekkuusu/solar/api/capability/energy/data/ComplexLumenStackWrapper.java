/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.energy.data;

import arekkuusu.solar.api.helper.NBTHelper;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 * <p>
 * Default implementation for {@link ItemStack} with a quantum entangled lumen storage
 */
public class ComplexLumenStackWrapper extends ComplexLumenWrapper {

	private final ItemStack stack;

	/**
	 * @param stack  The {@link ItemStack}
	 * @param max    Lumen capacity
	 */
	public ComplexLumenStackWrapper(ItemStack stack, int max) {
		super(max);
		this.stack = stack;
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public Optional<UUID> getKey() {
		return NBTHelper.getNBTTag(stack, ILumen.NBT_TAG)
				.map(nbtTagCompound -> nbtTagCompound.getUniqueId(ComplexLumenWrapper.NBT_ENTANGLED_TAG));
	}

	@Override
	public void setKey(UUID key) {
		stack.getOrCreateSubCompound(ILumen.NBT_TAG).setUniqueId(ComplexLumenWrapper.NBT_ENTANGLED_TAG, key);
	}
}
