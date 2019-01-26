package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PositionsNBTProvider implements ICapabilityProvider, INBTSerializable<NBTBase> {

	public final IPositionsNBTDataCapability instance;

	public PositionsNBTProvider(IPositionsNBTDataCapability instance) {
		this.instance = instance;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return Capabilities.POSITIONS == capability;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return Capabilities.POSITIONS == capability ? Capabilities.POSITIONS.cast(instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return Capabilities.POSITIONS.writeNBT(instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		Capabilities.POSITIONS.readNBT(instance, null, nbt);
	}
}
