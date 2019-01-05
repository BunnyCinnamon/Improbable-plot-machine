/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.handler.gen;

import arekkuusu.implom.common.lib.LibMod;
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
 * It's distributed as part of Improbable plot machine.
 */
public final class ModGen {

	public static final ResourceLocation SCHRODINGER_LOOT = load("schrodinger_eye");

	public static void init() {
		//World generators
		GameRegistry.registerWorldGenerator(new MonolithCubeStructure(), 5);
		GameRegistry.registerWorldGenerator(new MonolithObeliskDecorator(), 5);
		//Loot tables
		LootTableList.register(SCHRODINGER_LOOT);
	}

	private static ResourceLocation load(String name) {
		return new ResourceLocation(LibMod.MOD_ID, name);
	}

	public enum Structure {
		MONOLITH_CUBE_SMALL("monolith_cube_small"),
		MONOLITH_CUBE_HUGE("monolith_cube_huge"),
		MONOLITH_OBELISK_PIECE_BOTTOM("monolith_obelisk_piece_bottom"),
		MONOLITH_OBELISK_PIECE_THICC("monolith_obelisk_piece_thicc"),
		MONOLITH_OBELISK_PIECE_FLAT("monolith_obelisk_piece_flat"),
		MONOLITH_OBELISK_PIECE_TOP("monolith_obelisk_piece_top"),;

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
