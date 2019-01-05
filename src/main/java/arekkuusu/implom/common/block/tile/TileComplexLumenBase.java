package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.energy.data.ComplexLumenTileWrapper;
import arekkuusu.implom.api.capability.energy.data.IComplexLumen;
import arekkuusu.implom.api.capability.quantum.WorldData;
import arekkuusu.implom.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileComplexLumenBase extends TileBase {

	protected IComplexLumen handler;

	public TileComplexLumenBase() {
		this.handler = createHandler();
	}

	public abstract int getCapacity();

	public IComplexLumen createHandler() {
		return new ComplexLumenTileWrapper<>(this, getCapacity());
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY || capability == ModCapability.COMPLEX_LUMEN_CAPABILITY;
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.LUMEN_CAPABILITY
				? ModCapability.LUMEN_CAPABILITY.cast(handler) : capability == ModCapability.COMPLEX_LUMEN_CAPABILITY
				? ModCapability.COMPLEX_LUMEN_CAPABILITY.cast((IComplexLumen) handler) : null;
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasKey(WorldData.NBT_TAG)) {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			if(data.hasKey(IComplexLumen.NBT_TAG)) {
				NBTTagCompound tag = data.getCompoundTag(IComplexLumen.NBT_TAG);
				if(tag.hasUniqueId("key")) {
					handler.setKey(tag.getUniqueId("key"));
				} else handler.setKey(null);
			} else handler.setKey(null);
		} else handler.setKey(null);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		handler.getKey().ifPresent(key -> {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			NBTTagCompound tag = data.getCompoundTag(IComplexLumen.NBT_TAG);
			tag.setUniqueId("key", key);
			data.setTag(IComplexLumen.NBT_TAG, tag);
			compound.setTag(WorldData.NBT_TAG, data);
		});
	}
}
