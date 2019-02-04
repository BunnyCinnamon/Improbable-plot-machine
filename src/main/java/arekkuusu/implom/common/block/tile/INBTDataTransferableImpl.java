package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.INBTDataTransferable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public interface INBTDataTransferableImpl extends INBTDataTransferable {

	@Override
	default EnumActionResult init(NBTTagCompound compound) {
		boolean noKey = !compound.hasUniqueId("key");
		boolean override = getKey() == null && (noKey || !Objects.equals(compound.getUniqueId("key"), getKey()));
		if(override) {
			if(noKey) compound.setUniqueId("key", UUID.randomUUID());
			UUID uuid = compound.getUniqueId("key");
			setKey(uuid);
			return EnumActionResult.SUCCESS;
		} else if(noKey) {
			compound.setUniqueId("key", getKey());
			return EnumActionResult.PASS;
		}
		return EnumActionResult.FAIL;
	}

	void setKey(@Nullable UUID uuid);

	@Nullable
	UUID getKey();
}
