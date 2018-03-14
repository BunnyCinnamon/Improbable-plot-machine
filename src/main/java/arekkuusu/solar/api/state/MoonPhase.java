/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.state;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import java.util.Locale;

/**
 * Created by <Arekkuusu> on 30/12/2017.
 * It's distributed as part of Solar.
 */
public enum MoonPhase implements IStringSerializable {
	FULL_MOON,
	WANING_GIBBOUS,
	LAST_QUARTER,
	WANING_CRESCENT,
	NEW_MOON,
	WAXING_CRESCENT,
	FIRST_QUARTER,
	WAXING_GIBBOUS,
	//Other
	ECLIPSE;

	public final static PropertyEnum<MoonPhase> MOON_PHASE = PropertyEnum.create("phase", MoonPhase.class);

	public static MoonPhase getMoonPhase(World world) {
		int phase = world.provider.getMoonPhase(world.getWorldTime());
		if(phase > 7 || phase < 0) return ECLIPSE;
		return MoonPhase.values()[phase];
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
