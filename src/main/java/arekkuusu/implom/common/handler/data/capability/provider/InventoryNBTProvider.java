package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InventoryNBTProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

	public final IInventoryNBTDataCapability instance;

	public InventoryNBTProvider(IInventoryNBTDataCapability instance) {
		this.instance = instance;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return Capabilities.INVENTORY.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		Capabilities.INVENTORY.readNBT(instance, null, nbt);
	}
}
