/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler;

import arekkuusu.implom.common.lib.LibMod;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

import java.util.Map;

import static net.minecraftforge.common.config.Config.Comment;
import static net.minecraftforge.common.config.Config.LangKey;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Config(modid = LibMod.MOD_ID)
public final class ConfigHandler {

	@Comment("Enable/Disable/Play with structure spawn rates")
	@LangKey(LibMod.MOD_ID + ".config.gen")
	public static Gen GEN_CONFIG = new Gen();

	public static class Gen {

		@Comment("Enable/Disable/Play with monolith structures")
		public final MonolithConfig monolith = new MonolithConfig();
		@Comment("Edit loot dropped from monolith treasures")
		public final LootConfig loot = new LootConfig();

		public static class MonolithConfig {

			@Comment("Enable/Disable/Play with cube decorations")
			public final MonolithCubeDecorator cubeDecorator = new MonolithCubeDecorator();
			@Comment("Enable/Disable/Play with obelisk decorations")
			public final MonolithObeliskDecorator obeliskDecorator = new MonolithObeliskDecorator();

			public static class MonolithCubeDecorator {

				@Config.RequiresMcRestart
				@Comment("Edit weights of different cube decorations")
				public final CubeWeights weights = new CubeWeights();

				@Comment("Chance of monolith cubes to generate")
				@RangeDouble(min = 0, max = 100)
				public double rarity = 0.001;
				@Comment("If glyphs should generate")
				public boolean glyphs = true;
				@Comment("Chance of loot to generate")
				@RangeDouble(min = 0, max = 100)
				public double loot = 74;

				public static class CubeWeights {
					@Comment("Weight of monolithic cube small piece")
					@RangeInt(min = 0)
					public int cube_small = 10;
					@Comment("Weight of monolithic cube huge piece")
					@RangeInt(min = 0)
					public int cube_huge = 1;
				}
			}

			public static class MonolithObeliskDecorator {

				@Config.RequiresMcRestart
				@Comment("Edit weights of different obelisk decorations")
				public final ObeliskWeights weights = new ObeliskWeights();

				@Comment("Chance of monolith obelisks to generate")
				@RangeDouble(min = 0, max = 100)
				public double rarity = 0.05;
				@Comment("Chance of loot to generate")
				@RangeDouble(min = 0, max = 100)
				public double loot = 15;
				@Comment("Max amount of obelisks allowed in one chunk")
				@RangeInt(min = 0, max = 16)
				public int amount = 1;

				public static class ObeliskWeights {
					@Comment("Chance of monolithic obelisk generating top and bottom pieces")
					@RangeDouble(min = 0, max = 100)
					public double verticalExtremes = 50;
					@Comment("Max amount of individual pieces in a single obelisk")
					@RangeInt(min = 0, max = 10)
					public int height = 10;
					@Comment("Weight of monolithic obelisk thicc piece")
					@RangeInt(min = 0)
					public int obelisk_thicc = 4;
					@Comment("Weight of monolithic obelisk flat piece")
					@RangeInt(min = 0)
					public int obelisk_flat = 2;
				}
			}
		}

		public static class LootConfig {

			@Comment("Large pot loot weights")
			public final LargePotDecorator largePotDecorator = new LargePotDecorator();

			public static class LargePotDecorator {
				@Config.RequiresMcRestart
				public final Map<String, Double> weights = Maps.newHashMap(new ImmutableMap.Builder<String, Double>()
						.put("minecraft:bone", 10D)
						.put("minecraft:bowl", 8D)
						.put("minecraft:bread", 6D)
						.put("minecraft:gold_nugget", 5D)
						.put("minecraft:iron_nugget", 3.5D)
						.put("minecraft:gold_ingot", 2D)
						.put("minecraft:iron_ingot", 1D)
						.put("minecraft:emerald", 0.1D)
						.put("minecraft:diamond", 0.01D)
						.build()
				);
			}
		}
	}
}
