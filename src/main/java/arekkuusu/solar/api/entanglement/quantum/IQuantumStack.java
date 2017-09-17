/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement.quantum;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.util.helper.TooltipHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.CONTROL_KEY_DOWN;
import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.SHIFT_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public interface IQuantumStack extends IEntangledStack {

	@SideOnly(Side.CLIENT)
	default void addTooltipInfo(ItemStack stack, List<String> tooltip) {
		getKey(stack).ifPresent(uuid -> TooltipHelper.inline()
				.condition(SHIFT_KEY_DOWN)
				.ifAgrees(builder -> getInfo(builder, uuid)).build(tooltip));
	}

	@Override
	@SideOnly(Side.CLIENT)
	default TooltipHelper.Builder getInfo(TooltipHelper.Builder builder, UUID uuid) {
		builder.addI18("quantum_data", TooltipHelper.DARK_GRAY_ITALIC).end();
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

	@Override
	default void setKey(ItemStack stack, UUID uuid) {
		Optional<UUID> optional = getKey(stack);
		if(!optional.isPresent() || QuantumHandler.getQuantumStacks(optional.get()).isEmpty()) {
			NBTHelper.getOrCreate(stack, SolarApi.QUANTUM_DATA, NBTTagCompound::new).setUniqueId("key", uuid);
		}
	}
}
