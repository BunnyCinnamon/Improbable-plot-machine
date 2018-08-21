package arekkuusu.solar.common.item;

import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public interface IUUIDDescription {

	/**
	 * Adds information to the {@param tooltip} list
	 *
	 * @param uuid    The {@link UUID}
	 * @param tooltip The {@link List <String>}
	 */
	@SideOnly(Side.CLIENT)
	default void addInformation(UUID uuid, List<String> tooltip) {
		Tooltip.inline().condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(builder -> getInfo(builder, uuid)).apply()
				.build(tooltip);
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
