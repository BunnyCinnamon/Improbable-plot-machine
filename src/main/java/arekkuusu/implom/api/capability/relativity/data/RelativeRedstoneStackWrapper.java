package arekkuusu.implom.api.capability.relativity.data;

import arekkuusu.implom.api.capability.quantum.WorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class RelativeRedstoneStackWrapper extends RelativeStackWrapper implements IRelativeRedstone {

	public RelativeRedstoneStackWrapper(ItemStack stack) {
		super(stack);
	}

	@Override
	public void onPowerUpdate() {
		//FOR RENT
	}

	@Override
	public Optional<UUID> getKey() {
		NBTTagCompound root = getStack().getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		if(tag.hasKey(IRelativeRedstone.NBT_TAG)) {
			NBTTagCompound nbt = tag.getCompoundTag(IRelativeRedstone.NBT_TAG);
			return nbt.hasUniqueId("key") ? Optional.ofNullable(nbt.getUniqueId("key")) : Optional.empty();
		}
		return Optional.empty();
	}

	@Override
	public void setKey(@Nullable UUID key) {
		NBTTagCompound root = getStack().getOrCreateSubCompound("BlockEntityTag");
		NBTTagCompound tag = root.getCompoundTag(WorldData.NBT_TAG);
		NBTTagCompound nbt = tag.getCompoundTag(IRelativeRedstone.NBT_TAG);
		if(key != null) {
			nbt.setUniqueId("key", key);
		} else {
			nbt.removeTag("key");
		}
		tag.setTag(IRelativeRedstone.NBT_TAG, nbt);
		root.setTag(WorldData.NBT_TAG, tag);
	}
}
