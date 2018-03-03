/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.common.lib.LibMod;
import net.katsstuff.mirror.client.helper.ResourceHelperStatic;
import net.minecraft.util.SoundEvent;

/**
 * Created by <Arekkuusu> on 01/12/2017.
 * It's distributed as part of Solar.
 */
public class SoundBase extends SoundEvent {

	public SoundBase(String name) {
		super(ResourceHelperStatic.getSimple(LibMod.MOD_ID, name));
		setRegistryName(LibMod.MOD_ID, name);
	}
}
