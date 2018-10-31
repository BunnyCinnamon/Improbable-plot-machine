/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.capability.energy.data;

import arekkuusu.implom.api.capability.quantum.WorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 * <p>
 * Default implementation for {@link ItemStack} with a quantum entangled lumen storage
 */
public class ComplexLumenStackWrapper extends ComplexLumenWrapper {

	private final ItemStack stack;

	/**
	 * @param stack The {@link ItemStack}
	 * @param max   Lumen capacity
	 */
	public ComplexLumenStackWrapper(ItemStack stack, int max) {
		super(max);
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		if(tag.hasKey(IComplexLumen.NBT_TAG)) {
			NBTTagCompound nbt = tag.getCompoundTag(IComplexLumen.NBT_TAG);
			return nbt.hasUniqueId("key") ? Optional.ofNullable(nbt.getUniqueId("key")) : Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public void setKey(@Nullable UUID key) {
		NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		NBTTagCompound nbt = tag.getCompoundTag(IComplexLumen.NBT_TAG);
		if(key != null) {
			nbt.setUniqueId("key", key);
		} else {
			nbt.removeTag("key");
		}
		tag.setTag(IComplexLumen.NBT_TAG, nbt);
		root.setTag(WorldData.NBT_TAG, tag);
	}
}
