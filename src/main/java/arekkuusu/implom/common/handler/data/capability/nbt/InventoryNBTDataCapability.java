package arekkuusu.implom.common.handler.data.capability.nbt;

import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.UUID;

public class InventoryNBTDataCapability implements IInventoryNBTDataCapability {

	private UUID uuid;

	@Override
	public void onChange(ItemStack old) {
		//For Rent
	}

	@Override
	public void setKey(@Nullable UUID uuid) {
		this.uuid = uuid;
	}

	@Nullable
	@Override
	public UUID getKey() {
		return uuid;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		if(uuid != null) nbt.setUniqueId("key", uuid);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(nbt.hasUniqueId("key")) {
			uuid = nbt.getUniqueId("key");
		} else uuid = null;
	}
}
