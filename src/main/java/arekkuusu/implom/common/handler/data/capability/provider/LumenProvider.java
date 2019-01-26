package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.ILumenCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LumenProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

	public final ILumenCapability instance;

	public LumenProvider(ILumenCapability instance) {
		this.instance = instance;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == Capabilities.LUMEN;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == Capabilities.LUMEN ? Capabilities.LUMEN.cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return Capabilities.LUMEN.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		Capabilities.LUMEN.readNBT(instance, null, nbt);
	}
}
