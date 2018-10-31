/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.state;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;

/**
 * Created by <Arekkuusu> on 06/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class State {

	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

}
