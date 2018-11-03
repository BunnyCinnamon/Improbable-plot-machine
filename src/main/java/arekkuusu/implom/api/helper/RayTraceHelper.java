/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.api.helper;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public final class RayTraceHelper {

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
