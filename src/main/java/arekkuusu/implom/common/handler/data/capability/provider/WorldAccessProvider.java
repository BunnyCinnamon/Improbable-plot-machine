package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WorldAccessProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

	public final IWorldAccessNBTDataCapability instance;

	public WorldAccessProvider(IWorldAccessNBTDataCapability instance) {
		this.instance = instance;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == Capabilities.WORLD_ACCESS;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == Capabilities.WORLD_ACCESS ? Capabilities.WORLD_ACCESS.cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return Capabilities.WORLD_ACCESS.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		Capabilities.WORLD_ACCESS.readNBT(instance, null, nbt);
	}
}
