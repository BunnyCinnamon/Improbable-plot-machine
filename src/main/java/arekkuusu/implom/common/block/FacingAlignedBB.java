package arekkuusu.implom.common.block;

import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class FacingAlignedBB {

	private final Vector3 from, to;
	private EnumFacing defaultFacing;
	private EnumFacing[] facings = EnumFacing.values();

	public static FacingAlignedBB create(Vector3 from, Vector3 to, EnumFacing defaultFacing) {
		return new FacingAlignedBB(from, to).withDefault(defaultFacing);
	}

	public static AxisAlignedBB create(Vector3 from, Vector3 to) {
		return new AxisAlignedBB(from.divide(16).toVec3d(), to.divide(16).toVec3d());
	}

	private FacingAlignedBB(Vector3 from, Vector3 to) {
		this.from = from.asImmutable().divide(16);
		this.to = to.asImmutable().divide(16);
	}

	public FacingAlignedBB withDefault(EnumFacing defaultFacing) {
		this.defaultFacing = defaultFacing;
		return this;
	}

	public FacingAlignedBB withFacings(EnumFacing... facings) {
		this.facings = facings;
		return this;
	}

	public ImmutableMap<EnumFacing, AxisAlignedBB> build() {
		ImmutableMap.Builder<EnumFacing, AxisAlignedBB> builder = ImmutableMap.builder();
		for(EnumFacing facing : facings) {
			Vector3 from = this.from;
			Vector3 to = this.to;
			if(facing != defaultFacing) {
				switch (facing) {
					case DOWN:
					case UP:
						from = rotate(from, EnumFacing.Axis.X, 180F);
						to = rotate(to, EnumFacing.Axis.X, 180F);
						break;
					default:
						from = rotate(from, EnumFacing.Axis.Z, 90F);
						from = rotate(from, EnumFacing.Axis.Y, -facing.getHorizontalAngle());
						to = rotate(to, EnumFacing.Axis.Z, 90F);
						to = rotate(to, EnumFacing.Axis.Y, -facing.getHorizontalAngle());
						break;
				}
			}
			builder.put(facing, new AxisAlignedBB(from.toVec3d(), to.toVec3d()));
		}
		return builder.build();
	}

	private Vector3 rotate(Vector3 vec, EnumFacing.Axis axis, float angle) {
		Quat quat = Quat.fromAxisAngle(axis, angle);
		return vec.asMutable().subtractMutable(0.5D).rotate(quat).addMutable(0.5D).asImmutable();
	}
}
