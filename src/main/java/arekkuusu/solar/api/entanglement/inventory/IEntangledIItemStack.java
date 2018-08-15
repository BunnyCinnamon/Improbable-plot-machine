/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement.inventory;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledIItemStack extends IEntangledStack {

	/**
	 * Adds information to the {@param tooltip} list
	 *
	 * @param stack   The {@link ItemStack}
	 * @param tooltip The {@link List<String>}
	 */
	@SideOnly(Side.CLIENT)
	default void addTooltipInfo(ItemStack stack, List<String> tooltip) {
		getKey(stack).ifPresent(uuid -> Tooltip.inline()
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

	@Override
	default void setKey(ItemStack stack, UUID key) {
		Optional<UUID> optional = getKey(stack);
		if(!optional.isPresent()) {
			stack.getOrCreateSubCompound(EntangledIItemHandler.NBT_TAG).setUniqueId("key", key);
		}
	}
}
