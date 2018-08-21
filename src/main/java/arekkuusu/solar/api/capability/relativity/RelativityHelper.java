package arekkuusu.solar.api.capability.relativity;

import arekkuusu.solar.api.helper.NBTHelper;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

public final class RelativityHelper {

	public static Optional<UUID> getRelativeKey(ItemStack stack) {
		return Optional.ofNullable(NBTHelper.getUniqueID(stack, RelativityHandler.NBT_TAG));
	}

	public static void setRelativeKey(ItemStack stack, UUID key) {
		NBTHelper.setUniqueID(stack, RelativityHandler.NBT_TAG, key);
	}
}
