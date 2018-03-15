/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.inventory;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import net.katsstuff.mirror.client.helper.KeyCondition;
import net.katsstuff.mirror.client.helper.Tooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledIItemStack extends IEntangledStack {

	@SideOnly(Side.CLIENT)
	default void addTooltipInfo(ItemStack stack, List<String> tooltip) {
		getKey(stack).ifPresent(uuid -> Tooltip.inline()
				.condition(KeyCondition.ShiftKeyDown$.MODULE$)
				.ifTrueJ(builder -> getDetailedInfo(builder, EntangledIItemHandler.getEntanglement(uuid).stacks, uuid)).apply()
				.build(tooltip)
		);
	}

	@SideOnly(Side.CLIENT)
	default Tooltip getDetailedInfo(Tooltip builder, List<ItemStack> stacks, UUID uuid) {
		builder = builder.addI18n("tlp.quantum_data.name", Tooltip.DarkGrayItalic()).add(": ").newline();
		for(ItemStack stack : stacks) {
			builder = builder
					.add("    - ", TextFormatting.DARK_GRAY)
					.add(stack.getDisplayName(), Tooltip.GrayItalic())
					.add(" x " + stack.getCount()).newline();
		}
		Tooltip sub = builder.newline();
		return sub.condition(KeyCondition.ControlKeyDown$.MODULE$).ifTrueJ(b -> getInfo(sub, uuid)).apply();
	}

	default void setKey(ItemStack stack, UUID uuid) {
		Optional<UUID> optional = getKey(stack);
		if(!optional.isPresent()) {
			stack.getOrCreateSubCompound(EntangledIItemHandler.NBT_TAG).setUniqueId("key", uuid);
		}
	}
}
