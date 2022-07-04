package cinnamon.implom.common.handler.data.capability.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    public LinkedList<CapabilityWrapper<?, ?>> capabilities;
    public Map<CapabilityWrapper<?, ?>, LazyOptional<?>> providers;
    public ICapabilitySerializable<?> holder;

    public CapabilityProvider(ICapabilitySerializable<?> holder, LinkedList<CapabilityWrapper<?, ?>> capabilities) {
        this.capabilities = capabilities;
        this.providers = new LinkedHashMap<>();
        this.holder = holder;
    }

    public CapabilityProvider(ICapabilitySerializable<?> holder) {
        this.capabilities = new LinkedList<>();
        this.providers = new LinkedHashMap<>();
        this.holder = holder;
    }

    public <C> Optional<C> getAs(Class<C> cl) {
        return cl.isInstance(holder) ? Optional.of(cl.cast(holder)) : Optional.empty();
    }

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return capabilities.stream().filter(c -> c.capability == cap && (side == c.side || c.side == null)).findFirst()
				.map(w -> providers.containsKey(w) ? providers.get(w) : providers.put(w, LazyOptional.of(() -> w.instance)))
                .map(l -> (LazyOptional<T>) l)
				.orElse(LazyOptional.empty());
	}

    public <T> void invalidateAll() {
        capabilities.forEach(w -> {
            providers.computeIfPresent(w, (capabilityWrapper, lazyOptional) -> {
                lazyOptional.invalidate();
                return null;
            });
        });
    }

	public <T> void invalidate(Capability<T> cap) {
        capabilities.stream().filter(c -> c.capability == cap).findFirst().ifPresent(w -> {
            providers.computeIfPresent(w, (capabilityWrapper, lazyOptional) -> {
                lazyOptional.invalidate();
                return null;
            });
        });
    }

	/* NBT */
    public final static String TAG_LIST_SIZE = "size";
    public final static String TAG_LIST = "list";

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tagCompound = new CompoundTag();
        ListTag tagList = new ListTag();
        for (CapabilityWrapper<?, ?> wrapper : capabilities) {
            CompoundTag subTagCompound = new CompoundTag();
            Tag nbtBase = wrapper.serializeNBT();
            subTagCompound.put(wrapper.capability.getName(), nbtBase);
            tagList.add(subTagCompound);
        }
        tagCompound.put(TAG_LIST, tagList);
        tagCompound.putInt(TAG_LIST_SIZE, capabilities.size());
        return tagCompound;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int size = nbt.getInt(TAG_LIST_SIZE);
        ListTag tagList = nbt.getList(TAG_LIST, Tag.TAG_COMPOUND);
        for (int i = 0; i < size; i++) {
            CompoundTag subTagCompound = tagList.getCompound(i);
            Tag nbtBase = subTagCompound.get(capabilities.get(i).capability.getName());
            capabilities.get(i).deserializeNBT(nbtBase);
        }
    }

    public static class CapabilityWrapper<C extends INBTSerializable<T>, T extends Tag> implements INBTSerializable<Tag> {

        final Capability<?> capability;
        final C instance;
        final Direction side;

        public CapabilityWrapper(Capability<?> capability, C instance, Direction side) {
            this.capability = capability;
            this.instance = instance;
            this.side = side;
        }

        private T serialize() {
            return instance.serializeNBT();
        }

        private void deserialize(T nbt) {
            instance.deserializeNBT(nbt);
        }

        @Override
        public Tag serializeNBT() {
            return serialize();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void deserializeNBT(Tag nbt) {
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

        public <C extends INBTSerializable<T>, T extends Tag> Builder put(Capability<?> capability, C instance) {
            capabilities.add(new CapabilityWrapper<>(capability, instance, null));
            return this;
        }

        public <C extends INBTSerializable<T>, T extends Tag> Builder put(Capability<?> capability, C instance, Direction side) {
            capabilities.add(new CapabilityWrapper<>(capability, instance, side));
            return this;
        }

        public CapabilityProvider build() {
            return new CapabilityProvider(holder, capabilities);
        }
    }
}
