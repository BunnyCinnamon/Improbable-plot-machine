package arekkuusu.implom.common.item;

import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IUUIDDescription {

	Map<Character, String> CHARACTER_MAP = ImmutableMap.<Character, String>builder()
			.put('-', "þ")
			.put('0', "ð")
			.put('1', "Ʃ")
			.put('2', "Ƿ")
			.put('3', "ƴ")
			.put('4', "ƀ")
			.put('5', "Ϫ")
			.put('6', "ϗ")
			.put('7', "Ɔ")
			.put('8', "Ծ")
			.put('9', "ή")
			.build();

	/**
	 * Adds information to the {@param tooltip} list
	 *
	 * @param uuid    The {@link UUID}
	 * @param tooltip The {@link List <String>}
	 */
	@SideOnly(Side.CLIENT)
	default void addInformation(UUID uuid, List<String> tooltip, String group) {
		Tooltip.inline().condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(builder -> getInfo(builder.addI18n("tlp." + group + ".group"), uuid)).apply()
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
		StringBuilder characters = new StringBuilder();
		String key = String.valueOf(uuid.hashCode());
		for(char c : key.toCharArray()) {
			characters.append(CHARACTER_MAP.get(c));
		}
		return builder.space().add(">").space().add(characters.toString().replace("", " ").trim(), Tooltip.GrayItalic()).newline();
	}
}
