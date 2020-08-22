package arekkuusu.implom.common.handler.data.capability.provider;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Optional;

public class CapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

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

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return capabilities.stream().filter(c -> c.capability == cap).findFirst()
				.map(c -> LazyOptional.of(() -> (T) c.instance))
				.orElse(LazyOptional.empty());
	}

	/* NBT */
    public final static String TAG_LIST_SIZE = "size";
    public final static String TAG_LIST = "list";

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tagCompound = new CompoundNBT();
        ListNBT tagList = new ListNBT();
        for (CapabilityWrapper<?, ?> wrapper : capabilities) {
            CompoundNBT subTagCompound = new CompoundNBT();
            INBT nbtBase = wrapper.serializeNBT();
            subTagCompound.put(wrapper.capability.getName(), nbtBase);
            tagList.add(subTagCompound);
        }
        tagCompound.put(TAG_LIST, tagList);
        tagCompound.putInt(TAG_LIST_SIZE, capabilities.size());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        int size = nbt.getInt(TAG_LIST_SIZE);
        ListNBT tagList = nbt.getList(TAG_LIST, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < size; i++) {
            CompoundNBT subTagCompound = tagList.getCompound(i);
            INBT nbtBase = subTagCompound.get(capabilities.get(i).capability.getName());
            capabilities.get(i).deserializeNBT(nbtBase);
        }
    }

    public static class CapabilityWrapper<C extends INBTSerializable<T>, T extends INBT> implements INBTSerializable<INBT> {

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
        public INBT serializeNBT() {
            return serialize();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void deserializeNBT(INBT nbt) {
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

        public <C extends INBTSerializable<T>, T extends INBT> Builder put(Capability<?> capability, C instance) {
            capabilities.add(new CapabilityWrapper<>(capability, instance));
            return this;
        }

        public CapabilityProvider build() {
            return new CapabilityProvider(holder, capabilities);
        }
    }
}
