package arekkuusu.implom.common.handler.data.capability.provider;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Optional;

public class CapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {

	public LinkedList<CapabilityWrapper<?, ?>> capabilities;
	public ICapabilitySerializable<?> holder;

	public CapabilityProvider(ICapabilitySerializable<?> holder, LinkedList<CapabilityWrapper<?, ?>> capabilities) {
		this.capabilities = capabilities;
		this.holder = holder;
	}

	public CapabilityProvider(ICapabilitySerializable<?> holder) {
		this.capabilities = new LinkedList<>();
		this.holder = holder;
	}

	public <C> Optional<C> getAs(Class<C> cl) {
		return cl.isInstance(holder) ? Optional.of(cl.cast(holder)) : Optional.empty();
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capabilities.stream().anyMatch(c -> c.capability == capability);
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		//noinspection unchecked
		return capabilities.stream().filter(c -> c.capability == capability).findFirst()
				.map(c -> (T) capability.cast((T) c.instance))
				.orElse(null);
	}

	/* NBT */
	public final static String TAG_LIST_SIZE = "size";
	public final static String TAG_LIST = "list";

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		for(CapabilityWrapper<?, ?> wrapper : capabilities) {
			NBTTagCompound subTagCompound = new NBTTagCompound();
			NBTBase nbtBase = wrapper.serializeNBT();
			subTagCompound.setTag(wrapper.capability.getName(), nbtBase);
			tagList.appendTag(subTagCompound);
		}
		tagCompound.setTag(TAG_LIST, tagList);
		tagCompound.setInteger(TAG_LIST_SIZE, capabilities.size());
		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		int size = nbt.getInteger(TAG_LIST_SIZE);
		NBTTagList tagList = (NBTTagList) nbt.getTag(TAG_LIST);
		for(int i = 0; i < size; i++) {
			NBTTagCompound subTagCompound = (NBTTagCompound) tagList.get(i);
			NBTBase nbtBase = subTagCompound.getTag(capabilities.get(i).capability.getName());
			capabilities.get(i).deserializeNBT(nbtBase);
		}
	}

	public static class CapabilityWrapper<C extends INBTSerializable<T>, T extends NBTBase> implements INBTSerializable<NBTBase> {

		final Capability<?> capability;
		final C instance;

		public CapabilityWrapper(Capability<?> capability, C instance) {
			this.capability = capability;
			this.instance = instance;
		}

		private T serialize() {
			return instance.serializeNBT();
		}

		private void deserialize(T nbt) {
			instance.deserializeNBT(nbt);
		}

		@Override
		public NBTBase serializeNBT() {
			return serialize();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void deserializeNBT(NBTBase nbt) {
			deserialize((T) nbt);
		}
	}

	public static class Builder {
		final ICapabilitySerializable<?> holder;
		final LinkedList<CapabilityWrapper<?, ?>> capabilities;

		public Builder(ICapabilitySerializable<?> holder) {
			this.holder = holder;
			capabilities = new LinkedList<>();
		}

		public <C extends INBTSerializable<T>, T extends NBTBase> Builder put(Capability<?> capability, C instance) {
			capabilities.add(new CapabilityWrapper<>(capability, instance));
			return this;
		}

		public CapabilityProvider build() {
			return new CapabilityProvider(holder, capabilities);
		}
	}
}
