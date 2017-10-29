/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.state;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.IStringSerializable;

/**
 * Created by <Arekkuusu> on 06/09/2017.
 * It's distributed as part of Solar.
 */
public enum Power implements IStringSerializable {
	ON, OFF;

	public static final PropertyEnum<Power> POWER = PropertyEnum.create("power", Power.class);
	public static final PropertyInteger POWER_AMOUNT = PropertyInteger.create("power", 0, 15);

	public Power inverse() {
		return this == ON ? OFF : ON;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}
}
