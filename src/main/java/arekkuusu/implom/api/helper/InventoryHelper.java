/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Optional;

/**
 * Created by <Arekkuusu> on 14/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class InventoryHelper {

	public static Optional<IItemHandler> getCapability(ICapabilityProvider provider) {
		return getCapability(provider, null);
	}

	public static Optional<IItemHandler> getCapability(ICapabilityProvider provider, EnumFacing facing) {
		return provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)
				? Optional.ofNullable(provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing))
				: Optional.empty();
	}

	@SuppressWarnings({"ConstantConditions", "unchecked"})
	public static void handleItemTransfer(ICapabilityProvider provider, EntityPlayer player, EnumHand hand) {
		getCapability(provider).ifPresent(handler -> {
			ItemStack inserted = player.getHeldItem(hand);
			if(!inserted.isEmpty()) {
				for(int i = 0; i < handler.getSlots(); i++) {
					ItemStack test = handler.insertItem(i, inserted, true);
					if(test != inserted) {
						player.setHeldItem(hand, handler.insertItem(i, inserted, false));
						break;
					}
				}
			} else {
				for(int i = 0; i < handler.getSlots(); i++) {
					ItemStack test = handler.extractItem(i, handler.getSlotLimit(i), false);
					if(!test.isEmpty()) {
						player.setHeldItem(hand, test);
						break;
					}
				}
			}
		});
	}
}
