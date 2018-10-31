package arekkuusu.solar.api.capability.worldaccess.data;

import arekkuusu.solar.api.capability.quantum.WorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WorldAccessStackWrapper implements IWorldAccess {

	private final ItemStack stack;

	public WorldAccessStackWrapper(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public Optional<UUID> getKey() {
		NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		if(tag.hasKey(IWorldAccess.NBT_TAG)) {
			NBTTagCompound nbt = tag.getCompoundTag(IWorldAccess.NBT_TAG);
			return nbt.hasUniqueId("key") ? Optional.ofNullable(nbt.getUniqueId("key")) : Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public void setKey(@Nullable UUID key) {
		NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		NBTTagCompound nbt = tag.getCompoundTag(IWorldAccess.NBT_TAG);
		if(key != null) {
			nbt.setUniqueId("key", key);
		} else {
			nbt.removeTag("key");
		}
		tag.setTag(IWorldAccess.NBT_TAG, nbt);
		root.setTag(WorldData.NBT_TAG, tag);
	}
}
