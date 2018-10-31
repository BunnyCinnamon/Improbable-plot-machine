/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import arekkuusu.implom.client.util.ResourceLibrary;
import net.minecraft.util.ResourceLocation;

/**
 * Created by <Arekkuusu> on 01/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public enum Light {
	DULL(ResourceLibrary.DULL_PARTICLE, false),
	GLOW(ResourceLibrary.GLOW_PARTICLE, true);

	public final ResourceLocation location;
	public final boolean additive;

	Light(ResourceLocation location, boolean additive) {
		this.location = location;
		this.additive = additive;
	}
}
