/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.client.util.helper.TooltipHelper;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.UUID;

import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.CONTROL_KEY_DOWN;
import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public interface IQuantumStack extends IEntangledStack {

	int getSlots();

	@SideOnly(Side.CLIENT)
	default void addTooltipInfo(ItemStack stack, List<String> tooltip) {
		getKey(stack).ifPresent(uuid -> TooltipHelper.inline()
				.condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> getInfo(builder, uuid)).build(tooltip));
	}

	@Override
	@SideOnly(Side.CLIENT)
	default TooltipHelper.Builder getInfo(TooltipHelper.Builder builder, UUID uuid) {
		builder.addI18("quantum", TooltipHelper.DARK_GRAY_ITALIC).end();
		QuantumHandler.getQuantumStacks(uuid).forEach(item -> builder
				.add("    - ", TextFormatting.DARK_GRAY)
				.add(item.getDisplayName(), TooltipHelper.GRAY_ITALIC)
				.add(" x " + item.getCount()).end()
		);
		builder.skip();

		builder.condition(CONTROL_KEY_DOWN).ifAgrees(sub -> {
			sub.addI18("uuid_key", TooltipHelper.DARK_GRAY_ITALIC).add(": ").end();
			String key = uuid.toString();
			sub.add(" > ").add(key.substring(0, 18)).end();
			sub.add(" > ").add(key.substring(18)).end();
		});
		return builder;
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
}
