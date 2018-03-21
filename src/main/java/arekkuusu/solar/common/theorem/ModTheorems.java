/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.theorem;

import arekkuusu.solar.api.research.Theorem;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Created by <Arekkuusu> on 19/03/2018.
 * It's distributed as part of Solar.
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
