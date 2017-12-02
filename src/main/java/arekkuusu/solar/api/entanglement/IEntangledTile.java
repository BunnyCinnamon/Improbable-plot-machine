/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.entanglement;

import arekkuusu.solar.client.util.helper.TooltipBuilder;
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
	default TooltipBuilder getInfo(TooltipBuilder builder, UUID uuid) {
		builder.addI18("uuid_key", TooltipBuilder.DARK_GRAY_ITALIC).add(": ").end();
		String key = uuid.toString();
		builder.add(" > ").add(key.substring(0, 18)).end();
		builder.add(" > ").add(key.substring(18)).end();
		return builder;
	}

	Optional<UUID> getKey();

	void setKey(@Nullable UUID key);
}
