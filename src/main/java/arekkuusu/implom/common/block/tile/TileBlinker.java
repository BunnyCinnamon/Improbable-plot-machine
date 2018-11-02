/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.capability.relativity.data.IRelativeRedstone;
import arekkuusu.implom.api.capability.relativity.data.RelativeRedstoneTileWrapper;
import arekkuusu.implom.api.state.State;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class TileBlinker extends TileRelativeRedstoneBase {

	@Override
	public IRelativeRedstone createHandler() {
		return new RelativeRedstoneTileWrapper<TileBlinker>(this) {
			@Override
			public void onPowerUpdate() {
				if(getWorld() != null && !getWorld().isRemote) {
					int power = getRedstonePower();
					boolean wasPowered = isPowered();
					boolean isPowered = world.isBlockPowered(pos) && power > 0;
					if(isPowered != wasPowered && getPower() < power) {
						setPower(power, false);
					}
				}
			}
		};
	}

	public int getRedstonePower() {
		EnumFacing facing = getFacingLazy();
		return world.getRedstonePower(pos.offset(facing), facing);
	}

	private boolean isPoweredLazy() {
		return getStateValue(State.ACTIVE, pos).orElse(false);
	}

	private EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
