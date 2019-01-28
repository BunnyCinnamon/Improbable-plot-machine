/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.helper;

import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

import java.util.Map;

/**
 * Created by <Arekkuusu> on 19/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class FacingHelper {

	private static final Map<EnumFacing.Axis, Map<EnumFacing, Integer>> ROTATIONS = ImmutableMap.of(
			EnumFacing.Axis.Y, ImmutableMap.<EnumFacing, Integer>builder()
					.put(EnumFacing.EAST, 0)
					.put(EnumFacing.NORTH, 90)
					.put(EnumFacing.WEST, 180)
					.put(EnumFacing.SOUTH, 270)
					.build(),
			EnumFacing.Axis.X, ImmutableMap.<EnumFacing, Integer>builder()
					.put(EnumFacing.EAST, 0)
					.put(EnumFacing.UP, 90)
					.put(EnumFacing.WEST, 180)
					.put(EnumFacing.DOWN, 270)
					.build(),
			EnumFacing.Axis.Z, ImmutableMap.<EnumFacing, Integer>builder()
					.put(EnumFacing.NORTH, 0)
					.put(EnumFacing.UP, 90)
					.put(EnumFacing.SOUTH, 180)
					.put(EnumFacing.DOWN, 270)
					.build()
	);

	public static EnumFacing rotate(EnumFacing actual, EnumFacing from, EnumFacing to) {
		EnumFacing.Axis axis = from.getAxis().isHorizontal() && to.getAxis().isHorizontal() ? EnumFacing.Axis.Y : EnumFacing.Axis.X;
		if(from.getAxis().isVertical() && to.getAxis().isHorizontal()) {
			axis = to.getAxis();
		} else if(to.getAxis().isVertical() && from.getAxis().isHorizontal()) {
			axis = from.getAxis();
		}
		Vector3 originalVec = new Vector3.WrappedVec3i(actual.getDirectionVec()).asImmutable();
		int angle = ROTATIONS.get(axis).get(to) - ROTATIONS.get(axis).get(from);
		originalVec = originalVec.rotate(angle, axis);
		return EnumFacing.getFacingFromVector((float) originalVec.x(), (float) originalVec.y(), (float) originalVec.z());
	}

	public static Rotation getHorizontalRotation(EnumFacing from, EnumFacing to) {
		if(from != to && (!from.getAxis().isVertical() && !to.getAxis().isVertical())) {
			if(from.getOpposite() == to)
				return Rotation.CLOCKWISE_180;
			int indexFrom = from.getHorizontalIndex();
			int indexTo = to.getHorizontalIndex();
			if((indexFrom > indexTo || (from == EnumFacing.SOUTH && to == EnumFacing.EAST)))
				return from != EnumFacing.EAST || to != EnumFacing.SOUTH ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
			else {
				return Rotation.CLOCKWISE_90;
			}
		}
		return Rotation.NONE;
	}
}
