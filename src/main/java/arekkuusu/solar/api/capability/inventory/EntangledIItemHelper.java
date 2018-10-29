package arekkuusu.solar.api.capability.inventory;

import arekkuusu.solar.api.capability.inventory.data.IEntangledIItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Optional;

public final class EntangledIItemHelper {

	public static Optional<IEntangledIItemHandler> getCapability(ItemStack stack) {
		return stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
				? Optional.ofNullable(stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
				.filter(cap -> cap instanceof IEntangledIItemHandler)
				.map(cap -> ((IEntangledIItemHandler) cap))
				: Optional.empty();
	}

	public static <T extends IEntangledIItemHandler> Optional<T> getCapability(Class<T> cl, ItemStack stack) {
		return getCapability(stack).filter(cl::isInstance).map(cl::cast);
	}

	public static Optional<IEntangledIItemHandler> getCapability(TileEntity tile) {
		return tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
				? Optional.ofNullable(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
				.filter(cap -> cap instanceof IEntangledIItemHandler)
				.map(cap -> ((IEntangledIItemHandler) cap))
				: Optional.empty();
	}

	public static <T extends IEntangledIItemHandler> Optional<T> getCapability(Class<T> cl, TileEntity tile) {
		return getCapability(tile).filter(cl::isInstance).map(cl::cast);
	}
}
