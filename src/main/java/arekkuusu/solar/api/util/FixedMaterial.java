/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.util;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Solar.
 */
public class FixedMaterial extends Material {

	public final static Material DONT_MOVE = new FixedMaterial(MapColor.AIR).setImmovable().setRequiresTool();
	public final static Material BREAK = new FixedMaterial(MapColor.AIR).setBreakable().setRequiresTool();

	public FixedMaterial(MapColor color) {
		super(color);
	}

	private FixedMaterial setImmovable() {
		super.setImmovableMobility();
		return this;
	}

	private FixedMaterial setBreakable() {
		super.setNoPushMobility();
		return this;
	}
}
