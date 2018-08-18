/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import arekkuusu.solar.api.entanglement.energy.IPholarized;
import arekkuusu.solar.api.entanglement.energy.data.LumenTileWrapper;
import arekkuusu.solar.common.block.BlockNeutronBattery.BatteryCapacitor;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.block.BlockDirectional;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public class TileNeutronBattery extends TileBase implements IEntangledTile, IPholarized {

	private LumenTileWrapper<TileNeutronBattery> handler;
	private BatteryCapacitor capacitor;
	private UUID key;

	public TileNeutronBattery(BatteryCapacitor capacitor) {
		this.handler = new LumenTileWrapper<>(this, capacitor.getCapacity());
		this.capacitor = capacitor;
	}

	public TileNeutronBattery() {
	}

	public BatteryCapacitor getCapacitor() {
		return capacitor;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.NEUTRON_CAPABILITY && facing == getFacingLazy() || super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.NEUTRON_CAPABILITY && facing == getFacingLazy()
				? ModCapability.NEUTRON_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.DOWN);
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.ofNullable(key);
	}

	@Override
	public void setKey(@Nullable UUID key) {
		this.key = key;
		markDirty();
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		if(compound.hasUniqueId("key")) {
			this.key = compound.getUniqueId("key");
		}
		capacitor = new BatteryCapacitor();
		capacitor.deserializeNBT(compound.getCompoundTag("capacitor"));
		handler = new LumenTileWrapper<>(this, capacitor.getCapacity());
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		if(key != null) {
			compound.setUniqueId("key", key);
		}
		compound.setTag("capacitor", capacitor.serializeNBT());
	}
}
