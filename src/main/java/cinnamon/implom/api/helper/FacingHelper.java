package cinnamon.implom.api.helper;

import cinnamon.implom.api.util.Vector;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

import java.util.Map;

public final class FacingHelper {

    private static final Map<Direction.Axis, Map<Direction, Integer>> ROTATIONS = ImmutableMap.of(
            Direction.Axis.Y, ImmutableMap.<Direction, Integer>builder()
                    .put(Direction.EAST, 0)
                    .put(Direction.NORTH, 90)
                    .put(Direction.WEST, 180)
                    .put(Direction.SOUTH, 270)
                    .build(),
            Direction.Axis.X, ImmutableMap.<Direction, Integer>builder()
                    .put(Direction.EAST, 0)
                    .put(Direction.UP, 90)
                    .put(Direction.WEST, 180)
                    .put(Direction.DOWN, 270)
                    .build(),
            Direction.Axis.Z, ImmutableMap.<Direction, Integer>builder()
                    .put(Direction.NORTH, 0)
                    .put(Direction.UP, 90)
                    .put(Direction.SOUTH, 180)
                    .put(Direction.DOWN, 270)
                    .build()
    );

    public static Direction rotate(Direction actual, Direction from, Direction to) {
        Direction.Axis axis = from.getAxis().isHorizontal() && to.getAxis().isHorizontal() ? Direction.Axis.Y : Direction.Axis.X;
        if (from.getAxis().isVertical() && to.getAxis().isHorizontal()) {
            axis = to.getAxis();
        } else if (to.getAxis().isVertical() && from.getAxis().isHorizontal()) {
            axis = from.getAxis();
        }
        Vector originalVec = new Vector(actual.getNormal());
        int angle = ROTATIONS.get(axis).get(to) - ROTATIONS.get(axis).get(from);
        originalVec = originalVec.rotate(axis, angle);
        return Direction.getNearest((float) originalVec.x, (float) originalVec.y, (float) originalVec.z);
    }

    public static Rotation getHorizontalRotation(Direction from, Direction to) {
        if (from != to && (!from.getAxis().isVertical() && !to.getAxis().isVertical())) {
            if (from.getOpposite() == to)
                return Rotation.CLOCKWISE_180;
            int indexFrom = from.get2DDataValue();
            int indexTo = to.get2DDataValue();
            if ((indexFrom > indexTo || (from == Direction.SOUTH && to == Direction.EAST)))
                return from != Direction.EAST || to != Direction.SOUTH ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
            else {
                return Rotation.CLOCKWISE_90;
            }
        }
        return Rotation.NONE;
    }
}
