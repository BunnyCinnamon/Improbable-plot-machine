/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity;

import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by <Arekkuusu> on 08/07/2017.
 * It's distributed as part of Solar.
 */
public class ModEntities {

	private static int id;

	public static void init() {
		register(EntityFastItem.class, "fast_item");
		register(EntitySingularityItem.class, "singularity_item");
		register(EntityQuingentilliardItem.class, "quingentilliard_item");
		register(EntityEyeOfSchrodinger.class, "eye_of_schrodinger", 0x222222);
	}

	private static <T extends Entity> void register(Class<T> clazz, String name) {
		ResourceLocation location = new ResourceLocation(LibMod.MOD_ID, name);
		EntityRegistry.registerModEntity(location, clazz, name, id++, Solar.INSTANCE, 64, 1, true);
	}

	private static <T extends Entity> void register(Class<T> clazz, String name, int color) {
		ResourceLocation location = new ResourceLocation(LibMod.MOD_ID, name);
		EntityRegistry.registerModEntity(location, clazz, name, id++, Solar.INSTANCE, 64, 1, true, color, color);
	}
}
