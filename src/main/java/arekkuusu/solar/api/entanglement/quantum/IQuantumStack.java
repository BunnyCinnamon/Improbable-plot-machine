/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.client.util.helper.TooltipBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

import static arekkuusu.solar.client.util.helper.TooltipBuilder.Condition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public interface IQuantumStack extends IEntangledStack {

	@SideOnly(Side.CLIENT)
	default void addTooltipInfo(ItemStack stack, List<String> tooltip) {
		getKey(stack).ifPresent(uuid -> TooltipBuilder.inline()
				.condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> {
					getDetailedInfo(builder, QuantumHandler.getQuantumStacks(uuid), uuid);
				}).build(tooltip));
	}

	@SideOnly(Side.CLIENT)
	default void getDetailedInfo(TooltipBuilder builder, List<ItemStack> stacks, UUID uuid) {
		builder.addI18("quantum_data", TooltipBuilder.DARK_GRAY_ITALIC).end();
		stacks.forEach(item -> builder
				.add("    - ", TextFormatting.DARK_GRAY)
				.add(item.getDisplayName(), TooltipBuilder.GRAY_ITALIC)
				.add(" x " + item.getCount()).end()
		);
		builder.skip();
		getInfo(builder, uuid);
	}
}
