/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.proxy;

import arekkuusu.solar.api.helper.RayTraceHelper;
import arekkuusu.solar.api.util.Vector3;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = LibMod.MOD_ID, value = Side.CLIENT)
public class Events {

	//----------------Particle Renderer Start----------------//
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onTick(TickEvent.ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			ClientProxy.PARTICLE_RENDERER.update();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRenderAfterWorld(RenderWorldLastEvent event) {
		GlStateManager.pushMatrix();
		ClientProxy.PARTICLE_RENDERER.renderAll(event.getPartialTicks());
		GlStateManager.popMatrix();
	}
	//----------------Particle Renderer End----------------//

	@SubscribeEvent
	public static void renderGhostAngstrom(RenderWorldLastEvent event) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItemMainhand();
		if(stack.isEmpty() || stack.getItem() != ModItems.ANGSTROM) {
			stack = player.getHeldItemOffhand();
		}
		if(!stack.isEmpty() && stack.getItem() == ModItems.ANGSTROM) {
			RayTraceResult result = RayTraceHelper.tracePlayerHighlight(player);
			if(result.typeOfHit != RayTraceResult.Type.BLOCK) {
				Vector3 vec = Vector3.create(player.posX, player.posY + player.getEyeHeight(), player.posZ);
				vec.add(Vector3.create(player.getLookVec()).multiply(2.5D));
				BlockPos pos = new BlockPos(vec.toVec3d());
				IBlockState replaced = player.world.getBlockState(pos);
				if(player.world.isAirBlock(pos) || replaced.getBlock().isReplaceable(player.world, pos)) {
					RenderHelper.renderGhostBlock(pos, ModBlocks.ANGSTROM.getDefaultState());
				}
			}
		}
	}
}
