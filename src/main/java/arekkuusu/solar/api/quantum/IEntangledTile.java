/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.api.quantum;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledTile<T extends TileEntity> extends IEntangledInfo {

	@SuppressWarnings({"ConstantConditions", "unchecked"})
	default void handleItemTransfer(EntityPlayer player, EnumHand hand) {
		if(hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler handler = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
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
		}
	}

	@SuppressWarnings({"ConstantConditions", "unchecked"})
	default void takeItem(EntityPlayer player, ItemStack stack) {
		if(((T) this).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler handler = ((T) this).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
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

	@Nullable
	UUID getKey();

	void setKey(@Nullable UUID key);
}
