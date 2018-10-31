/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.energy.data.IComplexLumen;
import arekkuusu.implom.api.capability.quantum.WorldData;
import arekkuusu.implom.common.block.BlockNeutronBattery.BatteryCapacitor;
import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileNeutronBattery extends TileComplexLumenBase {

	private BatteryCapacitor capacitor;

	public TileNeutronBattery(BatteryCapacitor capacitor) {
		this.capacitor = capacitor;
		this.handler = createHandler();
	}

	public TileNeutronBattery() {
		this.capacitor = new BatteryCapacitor();
		this.handler = createHandler();
	}

	@Override
	public int getCapacity() {
		return capacitor != null ? capacitor.getCapacity() : 0;
	}

	public BatteryCapacitor getCapacitor() {
		return capacitor;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return facing == getFacingLazy() && super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, @Nullable EnumFacing facing) {
		return facing == getFacingLazy() ? super.getCapability(capability, facing) : null;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.DOWN);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		capacitor.deserializeNBT(compound.getCompoundTag(BatteryCapacitor.NBT_TAG));
		handler.setMax(capacitor.getCapacity());
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
		compound.setTag(BatteryCapacitor.NBT_TAG, capacitor.serializeNBT());
		handler.getKey().ifPresent(key -> {
			NBTTagCompound data = compound.getCompoundTag(WorldData.NBT_TAG);
			NBTTagCompound tag = data.getCompoundTag(IComplexLumen.NBT_TAG);
			tag.setUniqueId("key", key);
			data.setTag(IComplexLumen.NBT_TAG, tag);
			compound.setTag(WorldData.NBT_TAG, data);
		});
	}
}
