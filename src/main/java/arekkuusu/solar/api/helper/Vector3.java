/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.helper;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Random;

/**
 * Created by <Arekkuusu> on 06/09/2017.
 * It's distributed as part of Solar.
 */
public class Vector3 {

	private static final Random RAND = new Random();
	public double x;
	public double y;
	public double z;

	public Vector3(Vec3d vec) {
		this(vec.x, vec.y, vec.z);
	}

	public Vector3(Vec3i vec) {
		this(vec.getX(), vec.getY(), vec.getZ());
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3 setVec(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3 subtract(Vector3 vec) {
		return subtract(vec.x, vec.y, vec.z);
	}

	public Vector3 subtract(Vec3d vec) {
		return subtract(vec.x, vec.y, vec.z);
	}

	public Vector3 subtract(double x, double y, double z) {
		return add(-x, -y, -z);
	}

	public Vector3 add(Vector3 vec) {
		return add(vec.x, vec.y, vec.z);
	}

	public Vector3 add(Vec3d vec) {
		return add(vec.x, vec.y, vec.z);
	}

	public Vector3 add(double x, double y, double z) {
		return setVec(this.x + x, this.y + y, this.z + z);
	}

	public Vector3 multiply(Vector3 vec) {
		return multiply(vec.x, vec.y, vec.z);
	}

	public Vector3 multiply(double m) {
		return multiply(m, m, m);
	}

	public Vector3 multiply(double x, double y, double z) {
		return setVec(this.x * x, this.y * y, this.z * z);
	}

	public Vector3 divide(Vector3 vec) {
		return divide(vec.x, vec.y, vec.z);
	}

	public Vector3 divide(double m) {
		return divide(m, m, m);
	}

	public Vector3 divide(double x, double y, double z) {
		return setVec(this.x / x, this.y / y, this.z / z);
	}

	public Vector3 grow(double amount) {
		return add(amount, amount, amount);
	}

	public Vector3 shrink(double amount) {
		return subtract(amount, amount, amount);
	}

	public Vector3 rotateYaw(float angle) {
		float cos = MathHelper.cos(angle);
		float sin = MathHelper.sin(angle);
		double x = this.x * cos + this.z * sin;
		double z = this.z * cos - this.x * sin;
		return setVec(x, y, z);
	}

	public Vector3 rotatePitchZ(float angle) {
		float cos = MathHelper.cos(angle);
		float sin = MathHelper.sin(angle);
		double y = this.y * cos + this.z * sin;
		double z = this.z * cos - this.y * sin;
		return setVec(x, y, z);
	}

	public Vector3 rotatePitchX(float angle) {
		float cos = MathHelper.cos(angle);
		float sin = MathHelper.sin(angle);
		double y = this.y * cos + this.x * sin;
		double x = this.x * cos - this.y * sin;
		return setVec(x, y, z);
	}

	public Vector3 offset(EnumFacing facing, float amount) {
		return setVec(x + facing.getFrontOffsetX() * amount, y + facing.getFrontOffsetY() * amount, z + facing.getFrontOffsetZ() * amount);
	}

	public Vector3 normalize() {
		double root = MathHelper.sqrt(x * x + y * y + z * z);
		return root < 1.0E-4D ? setVec(0, 0, 0) : setVec(x / root, y / root, z / root);
	}

	public Vector3 cross(Vector3 vec) {
		double x = this.y * vec.z - this.z * vec.y;
		double y = this.z * vec.x - this.x * vec.z;
		double z = this.x * vec.y - this.y * vec.x;
		return setVec(x, y, z);
	}

	public double distanceTo(Vector3 vec) {
		double xDiff = x - vec.x;
		double yDiff = y - vec.y;
		double zDiff = z - vec.z;
		return MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
	}

	public Vector3 copy() {
		return new Vector3(x, y, z);
	}

	public ImmutableVector3 toImmutable() {
		return new ImmutableVector3(this);
	}

	public Vec3d toVec3d() {
		return new Vec3d(x, y, z);
	}

	public BlockPos toBlockPos() {
		return new BlockPos(x, y, z);
	}

	public static Vector3 getRandomVec(double max) {
		double x = max * (RAND.nextDouble() * 2 - 1);
		double y = max * (RAND.nextDouble() * 2 - 1);
		double z = max * (RAND.nextDouble() * 2 - 1);

		return new Vector3(x, y, z);
	}

	public static class ImmutableVector3 extends Vector3 {

		public static final ImmutableVector3 NULL = new Vector3(0, 0, 0).toImmutable();

		public ImmutableVector3(Vector3 vec) {
			super(vec.x, vec.y, vec.z);
		}

		public ImmutableVector3(Vec3d vec) {
			super(vec.x, vec.y, vec.z);
		}

		@Override
		public Vector3 setVec(double x, double y, double z) {
			return new Vector3(x, y, z);
		}
	}
}
