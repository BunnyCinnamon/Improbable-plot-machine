package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.api.capability.ILumenCapability;
import arekkuusu.implom.api.capability.data.WorldAccessNBTData;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.common.block.tile.TileNeutronBattery;
import arekkuusu.implom.common.handler.data.capability.InventoryNeutronCapability;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class NeutronProvider implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

	public final IWorldAccessNBTDataCapability worldAccessInstance = new WorldAccessNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			if(tile != null) {
				tile.markDirty();
			}
		}
	};
	public final InventoryNeutronCapability inventoryInstance = new InventoryNeutronCapability() {
		@Override
		protected void onChange() {
			if(tile != null) {
				tile.setActiveLazy(!getStackInSlot(0).isEmpty());
				tile.markDirty();
				tile.sync();
			}
		}
	};
	private final TileNeutronBattery tile;

	public NeutronProvider(TileNeutronBattery tile) {
		this.tile = tile;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return (capability == Capabilities.LUMEN && (tile == null || facing == tile.getFacingLazy()) && hasLumenReceptor())
				|| capability == Capabilities.WORLD_ACCESS
				|| capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
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
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return (capability == Capabilities.LUMEN && (tile == null || facing == tile.getFacingLazy()) && hasLumenReceptor())
				? Capabilities.LUMEN.cast(getLumenReceptor())
				: capability == Capabilities.WORLD_ACCESS
				? Capabilities.WORLD_ACCESS.cast(worldAccessInstance)
				: capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventoryInstance)
				: null;
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

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("worldaccess", Capabilities.WORLD_ACCESS.writeNBT(worldAccessInstance, null));
		tag.setTag("inventory", inventoryInstance.serializeNBT());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		NBTTagCompound tag = nbt != null ? nbt : new NBTTagCompound();
		Capabilities.WORLD_ACCESS.readNBT(worldAccessInstance, null, tag.getTag("worldaccess"));
		inventoryInstance.deserializeNBT(tag.getCompoundTag("inventory"));
	}
}
