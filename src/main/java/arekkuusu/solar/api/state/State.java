/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.state;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;

/**
 * Created by <Arekkuusu> on 06/09/2017.
 * It's distributed as part of Solar.
 */
public class State {

	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

}
