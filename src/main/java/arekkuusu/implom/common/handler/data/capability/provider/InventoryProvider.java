package arekkuusu.implom.common.handler.data.capability.provider;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InventoryProvider<T extends IItemHandler & INBTSerializable<NBTTagCompound>> implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	public final T instance;

	public InventoryProvider(T instance) {
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
	public NBTTagCompound serializeNBT() {
		return instance.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		instance.deserializeNBT(nbt);
	}
}
