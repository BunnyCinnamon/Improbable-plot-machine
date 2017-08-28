/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.api.state;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

/**
 * Created by <Arekkuusu> on 04/07/2017.
 * It's distributed as part of Solar.
 */
public enum Glyph implements IStringSerializable {
	A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P;

	public static final PropertyEnum<Glyph> GLYPH = PropertyEnum.create("glyph", Glyph.class);

	@Override
	public String getName() {
		return name().toLowerCase();
	}
}
