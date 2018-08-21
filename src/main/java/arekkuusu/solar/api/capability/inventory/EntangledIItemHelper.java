package arekkuusu.solar.api.capability.inventory;

import arekkuusu.solar.api.capability.inventory.data.IEntangledIItemHandler;
import net.minecraft.item.ItemStack;
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
}
