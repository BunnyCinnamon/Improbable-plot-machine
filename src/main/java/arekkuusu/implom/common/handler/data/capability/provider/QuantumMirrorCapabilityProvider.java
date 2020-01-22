package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.common.block.tile.TileQuantumMirror;
import arekkuusu.implom.common.handler.data.capability.nbt.InventoryNBTDataCapability;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.network.PacketHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class QuantumMirrorCapabilityProvider extends CapabilityProvider {

	public final InventoryNBTDataCapability inventoryNBTDataCapability = new InventoryNBTDataCapability() {

		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			return stack.getItem() != ModItems.QUANTUM_MIRROR;
		}

		@Override
		public void onChange(ItemStack old) {
			if(old.getItem() != getStackInSlot(0).getItem()) {
				if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					PacketHelper.sendQuantumMirrorPacket(this);
				}
			}
		}

		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			getAs(TileQuantumMirror.class).ifPresent(holder -> {
				holder.markDirty();
				holder.sync();
			});
		}
	};

	public QuantumMirrorCapabilityProvider(ICapabilitySerializable<?> holder) {
		super(holder);
		capabilities.add(new CapabilityWrapper<>(Capabilities.INVENTORY, inventoryNBTDataCapability));
	}
}
