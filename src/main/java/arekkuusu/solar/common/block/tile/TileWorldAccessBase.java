package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.quantum.WorldData;
import arekkuusu.solar.api.capability.worldaccess.data.IWorldAccess;
import arekkuusu.solar.api.capability.worldaccess.data.WorldAccessTileWrapper;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public abstract class TileWorldAccessBase extends TileBase {

	protected final IWorldAccess handler;

	public TileWorldAccessBase() {
		this.handler = createHandler();
	}

	public IWorldAccess createHandler() {
		return new WorldAccessTileWrapper<>(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.RELATIVE_WORLD_ACCESS_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.RELATIVE_WORLD_ACCESS_CAPABILITY
				? ModCapability.RELATIVE_WORLD_ACCESS_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasKey(WorldData.NBT_TAG)) {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			if(data.hasKey(IWorldAccess.NBT_TAG)) {
				NBTTagCompound tag = data.getCompoundTag(IWorldAccess.NBT_TAG);
				if(tag.hasUniqueId("key")) {
					handler.setKey(tag.getUniqueId("key"));
				} else handler.setKey(null);
			} else handler.setKey(null);
		} else handler.setKey(null);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		handler.getKey().ifPresent(key -> {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			NBTTagCompound tag = data.getCompoundTag(IWorldAccess.NBT_TAG);
			tag.setUniqueId("key", key);
			data.setTag(IWorldAccess.NBT_TAG, tag);
			compound.setTag(WorldData.NBT_TAG, data);
		});
	}
}
