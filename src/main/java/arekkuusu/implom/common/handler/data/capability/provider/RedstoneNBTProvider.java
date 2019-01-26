package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.nbt.IRedstoneNBTCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RedstoneNBTProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

	public final IRedstoneNBTCapability instance;

	public RedstoneNBTProvider(IRedstoneNBTCapability instance) {
		this.instance = instance;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return Capabilities.REDSTONE == capability;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return Capabilities.REDSTONE == capability ? Capabilities.REDSTONE.cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return Capabilities.REDSTONE.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		Capabilities.REDSTONE.readNBT(instance, null, nbt);
	}
}
