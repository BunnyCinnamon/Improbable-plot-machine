/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.util;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * Created by <Arekkuusu> on 06/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class IPMMaterial extends Material {

	public final static Material MONOLITH = new IPMMaterial(MapColor.BLACK).setImmovable().setRequiresTool();
	public final static Material FIRE_BRICK = new IPMMaterial(MapColor.BLACK).setImmovable().setRequiresTool();

	public IPMMaterial(MapColor color) {
		super(color);
	}

	private IPMMaterial setImmovable() {
		super.setImmovableMobility();
		return this;
	}

	private IPMMaterial setBreakable() {
		super.setNoPushMobility();
		return this;
	}
}
