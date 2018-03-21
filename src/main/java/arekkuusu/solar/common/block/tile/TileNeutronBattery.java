/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import arekkuusu.solar.api.entanglement.neutron.data.NeutronTileWrapper;
import arekkuusu.solar.common.handler.data.ModCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
public class TileNeutronBattery extends TileBase implements IEntangledTile {

	private NeutronTileWrapper<TileNeutronBattery> handler;
	private UUID key;

	public TileNeutronBattery(Capacity capacity) {
		handler = new NeutronTileWrapper<>(this, capacity.max);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.NEUTRON_CAPABILITY || hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ModCapability.NEUTRON_CAPABILITY
				? ModCapability.NEUTRON_CAPABILITY.cast(handler)
				: super.getCapability(capability, facing);
	}

	@Override
	public Optional<UUID> getKey() {
		return Optional.empty();
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
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		if(key != null) {
			compound.setUniqueId("key", key);
		}
	}

	public enum Capacity implements IStringSerializable {
		BLUE(64),
		GREEN(512),
		PINK(4096);

		public final int max;

		Capacity(int max) {
			this.max = max;
		}

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
