package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.inventory.EntangledIItemHandler;
import arekkuusu.solar.api.entanglement.inventory.IEntangledIItemStack;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public interface IHandlerEntangledDescription<T extends IEntangledIItemStack> extends IEntangledDescription<T> {

	@Override
	default void addTooltipInfo(T entangled, ItemStack stack, List<String> tooltip) {
		entangled.getKey(stack).ifPresent(uuid -> Tooltip.inline()
				.condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(builder ->
						getDetailedInfo(builder, EntangledIItemHandler.getEntanglement(uuid).stacks, uuid)
				).apply()
				.build(tooltip)
		);
	}

	/**
	 * Adds the quantum entangled inventory contents to the {@param builder}
	 *
	 * @param builder The {@link Tooltip} builder
	 * @param stacks  The {@link ItemStack}
	 * @param uuid    An {@link UUID}
	 * @return
	 */
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
		return sub.condition(KeyCondition$.MODULE$.controlKeyDown()).ifTrueJ(b -> getInfo(sub, uuid)).apply();
	}
}
