/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.api.quantum;

import arekkuusu.solar.client.util.helper.TooltipHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

import static arekkuusu.solar.client.util.helper.TooltipHelper.Condition.CONTROL_KEY_DOWN;

/**
 * Created by <Arekkuusu> on 02/09/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledInfo {

	@SideOnly(Side.CLIENT)
	default TooltipHelper.Builder getInfo(TooltipHelper.Builder builder, UUID uuid) {
		builder.addI18("quantum", TooltipHelper.DARK_GRAY_ITALIC).end();
		EntanglementHelper.getQuantumStacks(uuid).forEach(item -> builder
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
}
