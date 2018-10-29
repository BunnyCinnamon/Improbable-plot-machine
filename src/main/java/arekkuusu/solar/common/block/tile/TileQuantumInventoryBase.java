/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.inventory.data.EntangledTileWrapper;
import arekkuusu.solar.api.capability.inventory.data.IEntangledIItemHandler;
import arekkuusu.solar.api.capability.quantum.WorldData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 14/09/2017.
 * It's distributed as part of Solar.
 */
public abstract class TileQuantumInventoryBase extends TileBase {

	protected final IEntangledIItemHandler handler;

	public TileQuantumInventoryBase() {
		this.handler = createHandler();
	}

	public abstract int getCapacity();

	public IEntangledIItemHandler createHandler() {
		return new EntangledTileWrapper<>(this, getCapacity());
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	public void readNBT(NBTTagCompound compound) {
		if(compound.hasKey(WorldData.NBT_TAG)) {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			if(data.hasKey(IEntangledIItemHandler.NBT_TAG)) {
				NBTTagCompound tag = data.getCompoundTag(IEntangledIItemHandler.NBT_TAG);
				if(tag.hasUniqueId("key")) {
					handler.setKey(tag.getUniqueId("key"));
				} else handler.setKey(null);
			} else handler.setKey(null);
		} else handler.setKey(null);
	}

	@Override
	public void writeNBT(NBTTagCompound compound) {
		handler.getKey().ifPresent(key -> {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			NBTTagCompound tag = data.getCompoundTag(IEntangledIItemHandler.NBT_TAG);
			tag.setUniqueId("key", key);
			data.setTag(IEntangledIItemHandler.NBT_TAG, tag);
			compound.setTag(WorldData.NBT_TAG, data);
		});
	}
}
