/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Created by <Arekkuusu> on 01/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class SolarSounds {

	public static final SoundEvent SPARK = getRegisteredSound("spark");

	private static SoundEvent getRegisteredSound(String name) {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("improbableplotmachine", name));
	}
}
