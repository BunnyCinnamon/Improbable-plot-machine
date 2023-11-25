package cinnamon.implom.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;

import java.util.Random;

public class Vector {

    public static final Vector ZERO = new Vector(0, 0, 0);
    public static final Vector ONE = new Vector(1, 1, 1);
    public static final Vector X = new Vector(1, 0, 0);
    public static final Vector Y = new Vector(0, 1, 0);
    public static final Vector Z = new Vector(0, 0, 1);

    public static final Vector Forward = new Vector(0, 0, 1);
    public static final Vector Left = new Vector(-1, 0, 0);
    public static final Vector Right = new Vector(1, 0, 0);
    public static final Vector Backward = new Vector(0, 0, -1);

    public final double x;
    public final double y;
    public final double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public Vector(Vec3i vector3i) {
        this.x = vector3i.getX();
        this.y = vector3i.getY();
        this.z = vector3i.getZ();
    }

    public Vector subtract(Vector vec) {
        return this.addVector(-vec.x, -vec.y, -vec.z);
    }

    public Vector add(Vector vec) {
        return this.addVector(vec.x, vec.y, vec.z);
    }

    public Vector addVector(double x, double y, double z) {
        return new Vector(this.x + x, this.y + y, this.z + z);
    }

    public Vector multiply(Vector vec) {
        return new Vector(this.x * vec.x, this.y * vec.y, this.z * vec.z);
    }

    public Vector multiply(double factor) {
        return new Vector(this.x * factor, this.y * factor, this.z * factor);
    }

    public Vector divide(double divisor) {
        return new Vector(this.x / divisor, this.y / divisor, this.z / divisor);
    }

    public Vector cross(Vector vec) {
        return cross(vec.x, vec.y, vec.z);
    }

    public Vector cross(double x, double y, double z) {
        double newX = this.y * z - this.z * y;
        double newY = this.z * x - this.x * z;
        double newZ = this.x * y - this.y * x;
        return new Vector(newX, newY, newZ);
    }

    public Vector normalize() {
        double d0 = Mth.sqrt((float) (this.x * this.x + this.y * this.y + this.z * this.z));
        return d0 < 1.0E-4D ? ZERO : new Vector(this.x / d0, this.y / d0, this.z / d0);
    }

    public Vector offset(Vector direction, double distance) {
        return add(direction.multiply(distance));
    }

    public Vector rotateRandom(Random random, float angle) {
        Quat quatX = Quat.fromAxisAngleRad(Vector.X, (random.nextFloat() - 0.5F) * angle * (float) Math.PI / 180F);
        Quat quatY = Quat.fromAxisAngleRad(Vector.Y, (random.nextFloat() - 0.5F) * angle * (float) Math.PI / 180F);
        Quat quatZ = Quat.fromAxisAngleRad(Vector.Z, (random.nextFloat() - 0.5F) * angle * (float) Math.PI / 180F);
        Quat rotation = quatX.multiply(quatY).multiply(quatZ);
        return rotate(rotation);
    }

    public Vector rotate(Direction.Axis axis, float angle) {
        Quat quat = null;
        switch (axis) {
            case X:
                quat = Quat.fromAxisAngleRad(Vector.X, angle * (float) Math.PI / 180F);
                break;
            case Y:
                quat = Quat.fromAxisAngleRad(Vector.Y, angle * (float) Math.PI / 180F);
                break;
            case Z:
                quat = Quat.fromAxisAngleRad(Vector.Z, angle * (float) Math.PI / 180F);
                break;
        }
        return rotate(quat);
    }

    public Vector rotate(Quat quaternion) {
        double vx = x;
        double vy = y;
        double vz = z;
        double rx = quaternion.x;
        double ry = quaternion.y;
        double rz = quaternion.z;
        double rw = quaternion.w;

        double tx = 2 * (ry * vz - rz * vy);
        double ty = 2 * (rz * vx - rx * vz);
        double tz = 2 * (rx * vy - ry * vx);

        double cx = ry * tz - rz * ty;
        double cy = rz * tx - rx * tz;
        double cz = rx * ty - ry * tx;

        double newX = vx + rw * tx + cx;
        double newY = vy + rw * ty + cy;
        double newZ = vz + rw * tz + cz;

        return new Vector(newX, newY, newZ);
    }

    public Vector copy() {
        return new Vector(x, y, z);
    }

    public Vec3i toVector3i() {
        return new Vec3i((int) x, (int) y, (int) z);
    }

    public static Vector fromSpherical(double yaw, double pitch) {
        double clampedPitch = (pitch > 90F || pitch < -90F) ? Math.IEEEremainder(pitch, 180F) : pitch;
        double clampedYaw = (yaw > 180F || yaw < -180F) ? Math.IEEEremainder(yaw, 360F) : yaw;

        return fromSphericalRad((float) Math.toRadians(clampedYaw), (float) Math.toRadians(clampedPitch));
    }

    public static Vector fromSphericalRad(float yaw, float pitch) {
        double sinYaw = Mth.sin(yaw);
        double sinPitch = Mth.sin(pitch);

        double cosYaw = Mth.cos(yaw);
        double cosPitch = Mth.cos(pitch);

        return new Vector(-sinYaw * cosPitch, -sinPitch, cosYaw * cosPitch);
    }
}
