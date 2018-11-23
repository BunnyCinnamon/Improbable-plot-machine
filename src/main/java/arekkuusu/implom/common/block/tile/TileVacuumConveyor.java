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
import net.minecraft.util.ITickable;

/**
 * Created by <Arekkuusu> on 06/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileVacuumConveyor extends TileBase implements ITickable {

	@Override
	public void onLoad() {

	}

	@Override
	public void update() {

	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
