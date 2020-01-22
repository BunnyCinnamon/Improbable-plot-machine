package arekkuusu.implom.api.state.enums;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum Temperature implements IStringSerializable {
	RED,
	BLUE;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
