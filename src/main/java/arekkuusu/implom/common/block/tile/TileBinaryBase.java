/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.binary.data.BinaryTileWrapper;
import arekkuusu.implom.api.capability.binary.data.IBinary;
import arekkuusu.implom.api.capability.quantum.WorldData;
import arekkuusu.implom.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 20/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public abstract class TileBinaryBase extends TileBase {

	protected final BinaryTileWrapper<TileBinaryBase> handler;

	protected TileBinaryBase() {
		this.handler = createHandler();
	}

	public BinaryTileWrapper<TileBinaryBase> createHandler() {
		return new BinaryTileWrapper<>(this);
	}

	@Override
	public void onLoad() {
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
		return capability == ModCapability.BINARY_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.BINARY_CAPABILITY
				? ModCapability.BINARY_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasKey(WorldData.NBT_TAG)) {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			if(data.hasKey(IBinary.NBT_TAG)) {
				NBTTagCompound tag = data.getCompoundTag(IBinary.NBT_TAG);
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
			NBTTagCompound tag = data.getCompoundTag(IBinary.NBT_TAG);
			tag.setUniqueId("key", key);
			data.setTag(IBinary.NBT_TAG, tag);
			compound.setTag(WorldData.NBT_TAG, data);
		});
	}
}
