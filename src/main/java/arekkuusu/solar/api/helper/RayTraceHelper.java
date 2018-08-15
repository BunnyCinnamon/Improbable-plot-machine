/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.api.helper;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Solar.
 */
public final class RayTraceHelper {

	@Nullable
	public static RayTraceResult rayTraceAllAABB(List<AxisAlignedBB> boxes, BlockPos pos, Vec3d start, Vec3d end) {
		List<RayTraceResult> list = Lists.newArrayList();
		boxes.forEach(box -> list.add(rayTraceAABB(box, pos, start, end)));
		RayTraceResult result = null;
		double d1 = 0.0D;
		for(RayTraceResult raytraceresult : list) {
			if(raytraceresult != null) {
				double d0 = raytraceresult.hitVec.squareDistanceTo(end);

				if(d0 > d1) {
					result = raytraceresult;
					d1 = d0;
				}
			}
		}
		return result;
	}

	@Nullable
	public static RayTraceResult rayTraceAABB(AxisAlignedBB box, BlockPos pos, Vec3d start, Vec3d end) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		Vec3d a = start.subtract(x, y, z);
		Vec3d b = end.subtract(x, y, z);
		RayTraceResult result = box.calculateIntercept(a, b);
		return result == null ? null : new RayTraceResult(result.hitVec.addVector(x, y, z), result.sideHit, pos);
	}

	public static RayTraceResult tracePlayerHighlight(EntityPlayer player) {
		Vec3d eyes = player.getPositionEyes(1F);
		Vec3d look = player.getLookVec();
		double range = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		Vec3d hit = eyes.addVector(look.x * range, look.y * range, look.z * range);
		return player.world.rayTraceBlocks(eyes, hit, false, false, true);
	}

	@SideOnly(Side.CLIENT)
	public static RayTraceResult tracePlayerHighlight(EntityPlayerSP player) {
		Vec3d eyes = player.getPositionEyes(1F);
		Vec3d look = player.getLookVec();
		double range = Minecraft.getMinecraft().playerController.getBlockReachDistance();
		Vec3d hit = eyes.addVector(look.x * range, look.y * range, look.z * range);
		return player.world.rayTraceBlocks(eyes, hit, false, false, true);
	}
}
