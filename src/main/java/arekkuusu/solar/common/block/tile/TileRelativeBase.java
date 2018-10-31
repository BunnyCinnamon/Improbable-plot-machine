/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.quantum.WorldData;
import arekkuusu.solar.api.capability.relativity.data.IRelative;
import arekkuusu.solar.api.capability.relativity.data.RelativeTileWrapper;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/*
 * Created by <Arekkuusu> on 28/09/2017.
 * It's distributed as part of Solar.
 */
public abstract class TileRelativeBase extends TileBase {

	protected final IRelative handler;

	public TileRelativeBase() {
		this.handler = createHandler();
	}

	public IRelative createHandler() {
		return new RelativeTileWrapper<>(this);
	}

	@Override
	public void onLoad() {
		handler.add();
	}

	@Override
	public void validate() {
		super.validate();
		handler.add();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		handler.remove();
	}

	@Override
	public void onChunkUnload() {
		handler.remove();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.RELATIVE_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.RELATIVE_CAPABILITY
				? ModCapability.RELATIVE_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasKey(WorldData.NBT_TAG)) {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			if(data.hasKey(IRelative.NBT_TAG)) {
				NBTTagCompound tag = data.getCompoundTag(IRelative.NBT_TAG);
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
			NBTTagCompound tag = data.getCompoundTag(IRelative.NBT_TAG);
			tag.setUniqueId("key", key);
			data.setTag(IRelative.NBT_TAG, tag);
			compound.setTag(WorldData.NBT_TAG, data);
		});
	}
}
