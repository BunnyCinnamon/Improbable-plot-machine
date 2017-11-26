/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.handler;

import arekkuusu.solar.common.lib.LibMod;
import net.minecraftforge.common.config.Config;

import static net.minecraftforge.common.config.Config.*;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Config(modid = LibMod.MOD_ID)
public final class ConfigHandler {

	@Comment("Enable/Disable/Play with structure spawn rates")
	@RequiresMcRestart
	@LangKey(LibMod.MOD_ID + ".config.gen")
	public static Gen GEN_CONFIG = new Gen();

	public static class Gen {

		@Comment("Modify structure rarity and chest loot tables")
		public final AshenCubeStructureConfig ASHEN_CUBE_STRUCTURE = new AshenCubeStructureConfig();

		public static class AshenCubeStructureConfig {
			@Comment("Max amount of \"nuggets\" allowed in one Structure")
			public double size = 15;
			@Comment("Chance of 0-100% for loot")
			public double loot = 50;
			@Comment("Chance of 0-100% to generate")
			public double rarity = 10;
		}
	}
}
