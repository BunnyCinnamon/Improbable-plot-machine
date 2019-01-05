/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.energy.ILumenReciever;
import arekkuusu.implom.api.capability.energy.ILumenSender;
import arekkuusu.implom.common.block.BlockNeutronBattery.BatteryCapacitor;
import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileNeutronBattery extends TileComplexLumenBase implements ILumenReciever, ILumenSender {

	private BatteryCapacitor capacitor;

	@Override
	public int getCapacity() {
		return getCapacitor().map(BatteryCapacitor::getCapacity).orElse(0);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capacitor != null && facing == getFacingLazy() && super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <C> C getCapability(@Nonnull Capability<C> capability, @Nullable EnumFacing facing) {
		return capacitor != null && facing == getFacingLazy() ? super.getCapability(capability, facing) : null;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.DOWN);
	}

	public Optional<BatteryCapacitor> getCapacitor() {
		return Optional.ofNullable(capacitor);
	}

	public void setCapacitor(@Nullable BatteryCapacitor capacitor) {
		this.capacitor = capacitor;
		markDirty();
		sync();
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasKey(BatteryCapacitor.NBT_TAG)) {
			if(capacitor == null) {
				capacitor = BatteryCapacitor.fromOrdinal(compound.getInteger(BatteryCapacitor.NBT_TAG));
			}
			handler.setMax(capacitor.getCapacity());
		} else {
			handler.setMax(0);
			capacitor = null;
		}
		super.readNBT(compound);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		if(capacitor != null) {
			compound.setTag(BatteryCapacitor.NBT_TAG, capacitor.serializeNBT());
		}
		super.writeNBT(compound);
	}
}
