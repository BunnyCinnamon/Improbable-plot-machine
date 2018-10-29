/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.capability.inventory.data;

import arekkuusu.solar.api.capability.quantum.WorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
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
	 * @param stack The {@link ItemStack}
	 * @param slots Slot amount
	 */
	public EntangledStackWrapper(ItemStack stack, int slots) {
		super(slots);
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		if(tag.hasKey(IEntangledIItemHandler.NBT_TAG)) {
			NBTTagCompound nbt = tag.getCompoundTag(IEntangledIItemHandler.NBT_TAG);
			return nbt.hasUniqueId("key") ? Optional.ofNullable(nbt.getUniqueId("key")) : Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public void setKey(@Nullable UUID key) {
		NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		NBTTagCompound nbt = tag.getCompoundTag(IEntangledIItemHandler.NBT_TAG);
		if(key != null) {
			nbt.setUniqueId("key", key);
		} else {
			nbt.removeTag("key");
		}
		tag.setTag(IEntangledIItemHandler.NBT_TAG, nbt);
		root.setTag(WorldData.NBT_TAG, tag);
	}
}
