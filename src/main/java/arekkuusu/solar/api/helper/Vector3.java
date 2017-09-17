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

/**
 * Created by <Arekkuusu> on 06/09/2017.
 * It's distributed as part of Solar.
 */
public class Vector3 {

	public double x;
	public double y;
	public double z;

	public Vector3(Vec3d vec) {
		this(vec.x, vec.y, vec.y);
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

	public Vector3 subtract(double x, double y, double z) {
		return add(-x, -y, -z);
	}

	public Vector3 add(Vector3 vec) {
		return add(vec.x, vec.y, vec.z);
	}

	public Vector3 add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
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

	public Vector3 crossProduct(Vector3 vec) {
		double x = this.y * vec.z - this.z * vec.y;
		double y = this.z * vec.x - this.x * vec.z;
		double z = this.x * vec.y - this.y * vec.x;
		return setVec(x, y, z);
	}

	public Vector3 copy() {
		return new Vector3(x, y, z);
	}

	public Vec3d toVec3d() {
		return new Vec3d(x, y, z);
	}

	public BlockPos toShit() {
		return new BlockPos(x, y, z);
	}
}
