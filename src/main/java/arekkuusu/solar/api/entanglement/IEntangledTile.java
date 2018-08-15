/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.entanglement;

import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 04/09/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledTile {

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
		return builder.addI18n("tlp.uuid_key.name", Tooltip.DarkGrayItalic()).add(": ").skipLine()
				.add(" > ").add(key.substring(0, 18)).skipLine()
				.add(" > ").add(key.substring(18)).skipLine();
	}

	/**
	 * Gets the {@link UUID} if it exists
	 *
	 * @return An {@link Optional<UUID>} containing the key
	 */
	Optional<UUID> getKey();

	/**
	 * Sets the {@param key}
	 *
	 * @param key An {@link UUID}
	 */
	void setKey(@Nullable UUID key);
}
