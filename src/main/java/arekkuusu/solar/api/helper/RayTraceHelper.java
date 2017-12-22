/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Solar.
 */
public final class RayTraceHelper {

	public static RayTraceResult tracePlayerHighlight(EntityPlayerMP player) {
		Vec3d eyes = player.getPositionEyes(1F);
		Vec3d look = player.getLookVec();
		double range = player.interactionManager.getBlockReachDistance();
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
