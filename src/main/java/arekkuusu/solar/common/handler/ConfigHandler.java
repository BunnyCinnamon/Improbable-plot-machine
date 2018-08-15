/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.handler;

import arekkuusu.solar.common.lib.LibMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

import static net.minecraftforge.common.config.Config.Comment;
import static net.minecraftforge.common.config.Config.LangKey;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Config(modid = LibMod.MOD_ID)
public final class ConfigHandler {

	@Comment("Enable/Disable/Play with structure spawn rates")
	@LangKey(LibMod.MOD_ID + ".config.gen")
	public static Gen GEN_CONFIG = new Gen();

	public static class Gen {

		public final AshenCubeStructureConfig ashen_cube = new AshenCubeStructureConfig();
		public final MonolithConfig monolith = new MonolithConfig();

		public static class AshenCubeStructureConfig {

			@Config.RequiresMcRestart
			public final AshenWeights weights = new AshenWeights();

			@Comment("If the structure should generate underground only")
			public boolean underground = true;
			@Comment("Max amount of \"nuggets\" allowed in one Structure")
			@RangeInt(min = 0, max = 255)
			public int size = 25;
			@Comment("Height spread of \"nuggets\" in the Structure")
			@RangeInt(min = 0, max = 255)
			public double spread = 15;
			@Comment("Chance of 0-100% for loot")
			@RangeDouble(min = 0, max = 100)
			public double loot = 15;
			@Comment("Chance of 0-100% to generate")
			@RangeDouble(min = 0, max = 100)
			public double rarity = 0.01;

			public static class AshenWeights {
				@Comment("Weight of big \"nuggets\"")
				public int big = 10;
				@Comment("Weight of small \"nuggets\"")
				public int small = 6;
				@Comment("Weight of spawn \"nuggets\"")
				public int spawn = 1;
			}
		}

		public static class MonolithConfig {

			public final MonolithStructure structure = new MonolithStructure();
			public final ObeliskDecorator decorator = new ObeliskDecorator();

			public static class MonolithStructure {

				@Comment("Chance of 0-100% to generate")
				@RangeDouble(min = 0, max = 100)
				public double rarity = 0.001;
				@Comment("How buried in ruins is this structure 0-100%")
				@RangeInt(min = 0, max = 100)
				public int ruined = 64;
				@Comment("Chance of 0-100% for loot")
				@RangeDouble(min = 0, max = 100)
				public double loot = 15;
				public boolean holograth = true;
			}

			public static class ObeliskDecorator {

				@Config.RequiresMcRestart
				public final ObeliskWeights weights = new ObeliskWeights();

				@Comment("Chance of 0-100% to generate")
				@RangeDouble(min = 0, max = 100)
				public double rarity = 0.05;
				@Comment("Max amount of obelisks allowed in one chunk")
				@RangeInt(min = 0, max = 16)
				public int size = 1;

				public static class ObeliskWeights {
					@Comment("Weight of monolithic obelisk")
					public int monolithic = 2;
					@Comment("Weight of fragmented obelisk")
					public int fragmented = 1;
				}
			}
		}
	}
}
