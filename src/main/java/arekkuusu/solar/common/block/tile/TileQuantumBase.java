/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.inventory.EntangledIItemHandler;
import arekkuusu.solar.api.capability.inventory.data.EntangledTileWrapper;
import arekkuusu.solar.api.capability.inventory.data.IEntangledIItemHandler;
import arekkuusu.solar.api.capability.quantum.IQuantum;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 14/09/2017.
 * It's distributed as part of Solar.
 */
public abstract class TileQuantumBase extends TileBase implements IQuantum {

	private final IEntangledIItemHandler handler;
	private UUID key = null;

	public TileQuantumBase() {
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
		if(compound.hasUniqueId("key")) {
			setKey(compound.getUniqueId("key"));
		}
	}

	@Override
	public void writeNBT(NBTTagCompound compound) {
		getKey().ifPresent(key -> compound.setUniqueId("key", key));
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		if(!getKey().isPresent() || EntangledIItemHandler.getEntanglement(this.key).stacks.isEmpty()) {
			this.key = key;
			markDirty();
		}
	}
}