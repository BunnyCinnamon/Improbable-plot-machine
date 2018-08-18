package arekkuusu.solar.common.item;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public interface IEntangledDescription<T extends IEntangledStack> {

	/**
	 * Adds information to the {@param tooltip} list
	 *
	 * @param stack   The {@link ItemStack}
	 * @param tooltip The {@link List <String>}
	 */
	@SideOnly(Side.CLIENT)
	default void addTooltipInfo(T entangled, ItemStack stack, List<String> tooltip) {
		entangled.getKey(stack).ifPresent(uuid -> Tooltip.inline()
				.condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(builder -> getInfo(builder, uuid)).apply()
				.build(tooltip));
	}

	/**
	 * Adds the {@param uuid} information to the {@param builder}
	 *
	 * @param builder The {@link Tooltip} builder
	 * @param uuid    An {@link UUID}
	 * @return The tooltip with the {@param uuid} information
	 */
	@SideOnly(Side.CLIENT)
	default Tooltip getInfo(Tooltip builder, UUID uuid) {
		String key = uuid.toString();
		return builder.addI18n("tlp.uuid_key", Tooltip.DarkGrayItalic()).add(": ").newline()
				.add(" > ").add(key.substring(0, 18)).newline()
				.add(" > ").add(key.substring(18)).newline();
	}
}
