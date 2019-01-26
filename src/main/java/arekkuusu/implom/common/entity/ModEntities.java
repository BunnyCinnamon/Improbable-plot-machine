/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.entity;

import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by <Arekkuusu> on 08/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class ModEntities {

	private static int id;

	public static void register(IForgeRegistry<EntityEntry> registry) {
		registry.registerAll(
				create(EntityEyeOfSchrodinger.class, "eye_of_schrodinger").egg(0x222222, 0x222222).build(),
				create(EntityTemporalItem.class, "temporal_item").build(),
				create(EntityStaticItem.class, "static_item").build(),
				create(EntityLumen.class, "lumen").build()
		);
	}

	private static <T extends Entity> EntityEntryBuilder create(Class<T> clazz, String name) {
		return EntityEntryBuilder.create()
				.entity(clazz)
				.id(new ResourceLocation(LibMod.MOD_ID, name), id++)
				.name(name)
				.tracker(256, 1, true);
	}
}
