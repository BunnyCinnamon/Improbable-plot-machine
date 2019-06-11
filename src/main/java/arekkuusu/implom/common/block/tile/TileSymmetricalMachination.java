/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileSymmetricalMachination extends TileBase {

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
}
