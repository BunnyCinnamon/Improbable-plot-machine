/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.api.quantum;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.util.helper.TooltipHelper;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledStack extends IEntangledInfo {

	int getSlots();

	@SideOnly(Side.CLIENT)
	default void addTooltipInfo(ItemStack stack, List<String> tooltip) {
		getKey(stack).ifPresent(uuid -> TooltipHelper.inline()
				.condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> getInfo(builder, uuid)).build(tooltip));
	}

	@SuppressWarnings("ConstantConditions")
	default void handleItemTransfer(EntityPlayer player, World world, ItemStack container, EnumHand hand) {
		if(!container.isEmpty() && container.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			IItemHandler handler = container.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			ItemStack inserted = player.getHeldItem(hand);

			if(!inserted.isEmpty()) {
				for(int i = 0; i < handler.getSlots(); i++) {
					ItemStack test = handler.insertItem(i, inserted, true);
					if(test != inserted) {
						player.setHeldItem(hand, handler.insertItem(i, inserted, false));
						WorldQuantumData.get(world).markDirty();
						break;
					}
				}
			} else {
				for(int i = 0; i < handler.getSlots(); i++) {
					ItemStack test = handler.extractItem(i, player.isSneaking() ? handler.getSlotLimit(i) : 1, false);
					if(!test.isEmpty()) {
						player.setHeldItem(hand, test);
						WorldQuantumData.get(world).markDirty();
						break;
					}
				}
			}
		}
	}

	default void setKey(ItemStack stack, UUID uuid) {
		NBTTagCompound tag = stack.getOrCreateSubCompound(SolarApi.QUANTUM_DATA);
		tag.setUniqueId("key", uuid);
	}

	default Optional<UUID> getKey(ItemStack stack) {
		NBTTagCompound tag = NBTHelper.getNBT(stack, SolarApi.QUANTUM_DATA);
		return Optional.ofNullable(tag == null ? null : tag.getUniqueId("key"));
	}
}
