/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.theorem;

import arekkuusu.implom.api.research.Theorem;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Created by <Arekkuusu> on 19/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class ModTheorems {

	public static final ForgeRegistry<Theorem> REGISTRY = (ForgeRegistry<Theorem>) new RegistryBuilder<Theorem>()
			.setName(new ResourceLocation(LibMod.MOD_ID, "theorem"))
			.setType(Theorem.class)
			.setIDRange(0, 255)
			.create();

	public static void init() {

	}
}
