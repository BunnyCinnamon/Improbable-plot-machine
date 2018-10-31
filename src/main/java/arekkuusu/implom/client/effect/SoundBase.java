/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import arekkuusu.implom.common.lib.LibMod;
import net.katsstuff.teamnightclipse.mirror.client.helper.ResourceHelperStatic;
import net.minecraft.util.SoundEvent;

/*
 * Created by <Arekkuusu> on 01/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class SoundBase extends SoundEvent {

	public SoundBase(String name) {
		super(ResourceHelperStatic.getSimple(LibMod.MOD_ID, name));
		setRegistryName(LibMod.MOD_ID, name);
	}
}
