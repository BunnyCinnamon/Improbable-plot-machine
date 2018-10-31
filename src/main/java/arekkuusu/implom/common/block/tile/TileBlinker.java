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
					int power;
					if(getWorld().isBlockPowered(getPos()) && getPower() < (power = getRedstonePower())) {
						setPower(power, false);
					}
				}
			}
		};
	}

	public int getRedstonePower() {
		int power = 0;
		EnumFacing ignored = getFacingLazy();
		for(EnumFacing facing : EnumFacing.values()) {
			if(facing == ignored) continue;
			int detected = world.getRedstonePower(pos.offset(facing), facing);
			if(detected > power) power = detected;
		}
		return power;
	}

	private boolean isPoweredLazy() {
		return getStateValue(State.ACTIVE, pos).orElse(false);
	}

	private EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
