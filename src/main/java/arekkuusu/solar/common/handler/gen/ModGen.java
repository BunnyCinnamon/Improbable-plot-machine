/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.handler.gen;

import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by <Arekkuusu> on 01/11/2017.
 * It's distributed as part of Solar.
 */
public final class ModGen {

	public static final ResourceLocation SCHRODINGER_LOOT = load("schrodinger_eye");

	public static void init() {
		//World generators
		GameRegistry.registerWorldGenerator(new AshenCubeStructure(), 5);
		GameRegistry.registerWorldGenerator(new MonolithStructure(), 5);
		GameRegistry.registerWorldGenerator(new ObeliskDecorator(), 5);
		//Loot tables
		LootTableList.register(SCHRODINGER_LOOT);
	}

	private static ResourceLocation load(String name) {
		return new ResourceLocation(LibMod.MOD_ID, name);
	}

	public enum Structure {
		ASHEN_CUBE("ashen_cube"),
		ASHEN_CUBE_("ashen_cube_"),
		ASHEN_NUGGET_BIG("ashen_nugget_big"),
		ASHEN_NUGGET_SMALL("ashen_nugget_small"),
		ASHEN_NUGGET_SPAWN("ashen_nugget_spawn"),
		MONOLITH_CUBE("monolith_cube"),
		MONOLITH_OBELISK("monolith_obelisk"),
		MONOLITH_OBELISK_FRAGMENTED("monolith_obelisk_fragmented"),;

		private final ResourceLocation location;

		Structure(String name) {
			location = ModGen.load(name);
		}

		public Template load(World world) {
			TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
			return manager.getTemplate(world.getMinecraftServer(), location);
		}

		public void generate(World world, BlockPos pos, PlacementSettings settings) {
			Template template = load(world);
			template.addBlocksToWorld(world, pos, settings);
		}
	}
}
