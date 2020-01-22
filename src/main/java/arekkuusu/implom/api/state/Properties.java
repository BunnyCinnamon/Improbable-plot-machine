/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.state;

import arekkuusu.implom.api.state.enums.Direction;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Created by <Arekkuusu> on 06/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class Properties {

	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	public static final PropertyBool ACTIVE = PropertyBool.create("active");

	public static class UnlistedDirection implements IUnlistedProperty<Direction> {

		final String name;

		public UnlistedDirection(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isValid(Direction value) {
			return true;
		}

		@Override
		public Class<Direction> getType() {
			return Direction.class;
		}

		@Override
		public String valueToString(Direction value) {
			return value.getName();
		}
	}

	public static class UnlistedBoolean implements IUnlistedProperty<Boolean> {

		final String name;

		public UnlistedBoolean(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isValid(Boolean value) {
			return true;
		}

		@Override
		public Class<Boolean> getType() {
			return Boolean.class;
		}

		@Override
		public String valueToString(Boolean value) {
			return value.toString();
		}
	}
}
