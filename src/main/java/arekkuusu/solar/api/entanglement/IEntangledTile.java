/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement;

import net.katsstuff.mirror.client.helper.Tooltip;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 04/09/2017.
 * It's distributed as part of Solar.
 */
public interface IEntangledTile {

	@SideOnly(Side.CLIENT)
	default Tooltip getInfo(Tooltip builder, UUID uuid) {
		String key = uuid.toString();
		return builder.addI18n("tlp.uuid_key.name", Tooltip.DarkGrayItalic()).add(": ").skipLine()
				.add(" > ").add(key.substring(0, 18)).skipLine()
				.add(" > ").add(key.substring(18)).skipLine();
	}

	Optional<UUID> getKey();

	void setKey(@Nullable UUID key);
}
