/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.helper;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

import static net.minecraft.util.EnumFacing.*;

/**
 * Created by <Arekkuusu> on 19/01/2018.
 * It's distributed as part of Solar.
 */
public final class FacingHelper {

	public static EnumFacing rotateXY(EnumFacing actual, EnumFacing.AxisDirection direction, EnumFacing facing) {
		boolean inverse = direction == EnumFacing.AxisDirection.POSITIVE;
		switch(facing) {
			case NORTH:
			case SOUTH:
				inverse = inverse ? facing == SOUTH : facing == NORTH;
				actual = rotateX(actual, inverse);
				break;
			case WEST:
			case EAST:
				inverse = inverse ? facing == EAST : facing == WEST;
				actual = rotateZ(actual, inverse);
				break;
		}
		return actual;
	}

	public static EnumFacing rotateX(EnumFacing facing, boolean inverse) {
		if(!inverse) {
			return facing.rotateAround(Axis.X);
		} else {
			switch(facing) {
				case NORTH:
					return UP;
				case SOUTH:
					return DOWN;
				case UP:
					return SOUTH;
				case DOWN:
					return NORTH;
				case EAST:
				case WEST:
				default:
					throw new IllegalStateException("Unable to get X-rotated facing of " + facing);
			}
		}
	}

	public static EnumFacing rotateZ(EnumFacing facing, boolean inverse) {
		if(!inverse) {
			return facing.rotateAround(Axis.Z);
		} else {
			switch(facing) {
				case EAST:
					return UP;
				case WEST:
					return DOWN;
				case UP:
					return WEST;
				case DOWN:
					return EAST;
				case SOUTH:
				default:
					throw new IllegalStateException("Unable to get Z-rotated facing of " + facing);
			}
		}
	}

	public static Rotation getHorizontalRotation(EnumFacing from, EnumFacing to) {
		if(from != to && (!from.getAxis().isVertical() && !to.getAxis().isVertical())) {
			if(from.getOpposite() == to)
				return Rotation.CLOCKWISE_180;
			int indexFrom = from.getHorizontalIndex();
			int indexTo = to.getHorizontalIndex();
			if((indexFrom > indexTo || (from == SOUTH && to == EAST)))
				return from != EAST || to != SOUTH ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
			else {
				return Rotation.CLOCKWISE_90;
			}
		}
		return Rotation.NONE;
	}
}
