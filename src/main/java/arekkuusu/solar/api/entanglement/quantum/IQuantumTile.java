/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.entanglement.IEntangledTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public interface IQuantumTile extends IEntangledTile {

	default void takeItem(EntityPlayer player) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if(hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler handler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(handler == null) return;

			ItemStack contained = handler.extractItem(0, Integer.MAX_VALUE, false);
			if(stack.isEmpty()) {
				player.setHeldItem(EnumHand.MAIN_HAND, contained);
			} else {
				ItemHandlerHelper.giveItemToPlayer(player, contained);
			}
		}
	}

	boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing);

	@Nullable
	<C> C getCapability(Capability<C> capability, @Nullable EnumFacing facing);
}
