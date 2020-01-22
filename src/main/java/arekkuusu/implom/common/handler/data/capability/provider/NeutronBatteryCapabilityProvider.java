package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.ILumenCapability;
import arekkuusu.implom.api.capability.data.WorldAccessNBTData;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.common.block.tile.TileNeutronBattery;
import arekkuusu.implom.common.handler.data.capability.InventoryNeutronCapability;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class NeutronBatteryCapabilityProvider extends CapabilityProvider {

	public final IWorldAccessNBTDataCapability worldAccessInstance = new WorldAccessNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			getAs(TileNeutronBattery.class).ifPresent(holder -> {
				holder.markDirty();
				holder.sync();
			});
		}
	};
	public final InventoryNeutronCapability inventoryInstance = new InventoryNeutronCapability() {
		@Override
		protected void onChange() {
			getAs(TileNeutronBattery.class).ifPresent(holder -> {
				holder.setActiveLazy(!getStackInSlot(0).isEmpty());
				holder.markDirty();
				holder.sync();
			});
		}
	};

	public NeutronBatteryCapabilityProvider(ICapabilitySerializable<NBTTagCompound> holder) {
		super(holder);
		capabilities.add(new CapabilityWrapper<>(Capabilities.WORLD_ACCESS, worldAccessInstance));
		capabilities.add(new CapabilityWrapper<>(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventoryInstance));
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return (capability == Capabilities.LUMEN && ((facing == null || getAs(TileNeutronBattery.class).filter(f -> f.getFacingLazy() == facing).isPresent()) && hasLumenReceptor()))
				|| capability == Capabilities.WORLD_ACCESS
				|| capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return (capability == Capabilities.LUMEN && ((facing == null || getAs(TileNeutronBattery.class).filter(f -> f.getFacingLazy() == facing).isPresent()) && hasLumenReceptor()))
				? Capabilities.LUMEN.cast(getLumenReceptor())
				: capability == Capabilities.WORLD_ACCESS
				? Capabilities.WORLD_ACCESS.cast(worldAccessInstance)
				: capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryInstance)
				: null;
	}

	private boolean hasLumenReceptor() {
		if(inventoryInstance.getStackInSlot(0).isEmpty()) return false;
		WorldAccessNBTData data = worldAccessInstance.get();
		if(data == null || data.getPos() == null || data.getWorld() == null || data.getFacing() == null) return false;
		return Optional.ofNullable(data.getWorld().getTileEntity(data.getPos()))
				.map(tile -> tile.hasCapability(Capabilities.LUMEN, data.getFacing()))
				.orElse(false);
	}

	@Nullable
	private ILumenCapability getLumenReceptor() {
		if(inventoryInstance.getStackInSlot(0).isEmpty()) return null;
		WorldAccessNBTData data = worldAccessInstance.get();
		if(data == null || data.getPos() == null || data.getWorld() == null || data.getFacing() == null) return null;
		return Optional.ofNullable(data.getWorld().getTileEntity(data.getPos()))
				.map(tile -> tile.getCapability(Capabilities.LUMEN, data.getFacing()))
				.orElse(null);
	}
}
