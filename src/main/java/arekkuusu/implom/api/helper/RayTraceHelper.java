/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.helper;

import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class RayTraceHelper {

	@Nullable
	public static RayTraceResult rayTraceBlocksExcept(World world, Vector3 vecFrom, Vector3 vecTo, Predicate<IBlockState> predicate) {
		if(!Double.isNaN(vecFrom.x()) && !Double.isNaN(vecFrom.y()) && !Double.isNaN(vecFrom.z())) {
			if(!Double.isNaN(vecTo.x()) && !Double.isNaN(vecTo.y()) && !Double.isNaN(vecTo.z())) {
				int i = MathHelper.floor(vecTo.x());
				int j = MathHelper.floor(vecTo.y());
				int k = MathHelper.floor(vecTo.z());
				int l = MathHelper.floor(vecFrom.x());
				int i1 = MathHelper.floor(vecFrom.y());
				int j1 = MathHelper.floor(vecFrom.z());
				BlockPos blockpos = new BlockPos(l, i1, j1);
				IBlockState iblockstate = world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();

				if((!predicate.test(iblockstate) && iblockstate.getCollisionBoundingBox(world, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, false)) {
					return iblockstate.collisionRayTrace(world, blockpos, vecFrom.toVec3d(), vecTo.toVec3d());
				}

				int k1 = 200;

				while(k1-- >= 0) {
					if(Double.isNaN(vecFrom.x()) || Double.isNaN(vecFrom.y()) || Double.isNaN(vecFrom.z())) {
						return null;
					}

					if(l == i && i1 == j && j1 == k) {
						return null;
					}

					boolean flag2 = true;
					boolean flag = true;
					boolean flag1 = true;
					double d0 = 999.0D;
					double d1 = 999.0D;
					double d2 = 999.0D;

					if(i > l) {
						d0 = (double) l + 1.0D;
					} else if(i < l) {
						d0 = (double) l + 0.0D;
					} else {
						flag2 = false;
					}

					if(j > i1) {
						d1 = (double) i1 + 1.0D;
					} else if(j < i1) {
						d1 = (double) i1 + 0.0D;
					} else {
						flag = false;
					}

					if(k > j1) {
						d2 = (double) j1 + 1.0D;
					} else if(k < j1) {
						d2 = (double) j1 + 0.0D;
					} else {
						flag1 = false;
					}

					double d3 = 999.0D;
					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = vecTo.x() - vecFrom.x();
					double d7 = vecTo.y() - vecFrom.y();
					double d8 = vecTo.z() - vecFrom.z();

					if(flag2) {
						d3 = (d0 - vecFrom.x()) / d6;
					}

					if(flag) {
						d4 = (d1 - vecFrom.y()) / d7;
					}

					if(flag1) {
						d5 = (d2 - vecFrom.z()) / d8;
					}

					if(d3 == -0.0D) {
						d3 = -1.0E-4D;
					}

					if(d4 == -0.0D) {
						d4 = -1.0E-4D;
					}

					if(d5 == -0.0D) {
						d5 = -1.0E-4D;
					}

					EnumFacing enumfacing;

					if(d3 < d4 && d3 < d5) {
						enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
						vecFrom = new Vector3(d0, vecFrom.y() + d7 * d3, vecFrom.z() + d8 * d3);
					} else if(d4 < d5) {
						enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
						vecFrom = new Vector3(vecFrom.x() + d6 * d4, d1, vecFrom.z() + d8 * d4);
					} else {
						enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
						vecFrom = new Vector3(vecFrom.x() + d6 * d5, vecFrom.y() + d7 * d5, d2);
					}

					l = MathHelper.floor(vecFrom.x()) - (enumfacing == EnumFacing.EAST ? 1 : 0);
					i1 = MathHelper.floor(vecFrom.y()) - (enumfacing == EnumFacing.UP ? 1 : 0);
					j1 = MathHelper.floor(vecFrom.z()) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
					blockpos = new BlockPos(l, i1, j1);
					IBlockState state = world.getBlockState(blockpos);
					Block block1 = state.getBlock();

					if(!predicate.test(state) && block1.canCollideCheck(state, false)) {
						return state.collisionRayTrace(world, blockpos, vecFrom.toVec3d(), vecTo.toVec3d());
					}
				}
				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Nullable
	public static RayTraceResult rayTraceAllAABB(List<AxisAlignedBB> boxes, BlockPos pos, Vec3d start, Vec3d end) {
		List<RayTraceResult> results = Lists.newArrayList();
		boxes.forEach(box -> {
			RayTraceResult result = rayTraceAABB(box, pos, start, end);
			if(result != null) {
				results.add(result);
			}
		});
		RayTraceResult result = null;
		double otherSqtDis = 0.0D;
		for(RayTraceResult raytraceresult : results) {
			double sqtDis = raytraceresult.hitVec.squareDistanceTo(end);
			if(sqtDis > otherSqtDis) {
				result = raytraceresult;
				otherSqtDis = sqtDis;
			}
		}
		return result;
	}

	@Nullable
	public static RayTraceResult rayTraceAABB(AxisAlignedBB box, BlockPos pos, Vec3d start, Vec3d end) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		Vec3d aVec = start.subtract(x, y, z);
		Vec3d bVec = end.subtract(x, y, z);
		RayTraceResult result = box.calculateIntercept(aVec, bVec);
		return result == null ? null : new RayTraceResult(result.hitVec.addVector(x, y, z), result.sideHit, pos);
	}

	public static RayTraceResult tracePlayerHighlight(EntityPlayer player) {
		Vec3d eyes = player.getPositionEyes(1F);
		Vec3d look = player.getLookVec();
		double range = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		Vec3d hit = eyes.addVector(look.x * range, look.y * range, look.z * range);
		return player.world.rayTraceBlocks(eyes, hit, false, false, true);
	}

	public static boolean isHittingSideOfBlock(RayTraceResult hit, EnumFacing side, World world, IBlockState state, BlockPos pos) {
		if(hit != null && RayTraceResult.Type.BLOCK == hit.typeOfHit && side == hit.sideHit) {
			final BlockPos hitPos = hit.getBlockPos();
			return pos.equals(hitPos) && state == world.getBlockState(hitPos);
		}
		return false;
	}
}
