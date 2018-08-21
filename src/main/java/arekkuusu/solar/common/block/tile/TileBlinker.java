/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.capability.relativity.IRelativeRedstone;
import arekkuusu.solar.api.capability.relativity.RelativityHandler;
import arekkuusu.solar.api.state.State;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
public class TileBlinker extends TileRelativeBase implements IRelativeRedstone {

	@Override
	public void onPowerUpdate() {
		int power;
		if(world.isBlockPowered(pos) && RelativityHandler.getPower(this) < (power = getRedstonePower())) {
			RelativityHandler.setPower(this, power, false);
		}
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

	@Override
	public void onLoad() {
		if(!world.isRemote) {
			onPowerUpdate();
		}
	}

	@Override
	public void remove() {
		if(!world.isRemote) {
			RelativityHandler.removeRelative(this, () -> {
				if(world.isBlockPowered(getPos())) {
					RelativityHandler.setPower(this, 0, true);
				}
			});
		}
	}
}
