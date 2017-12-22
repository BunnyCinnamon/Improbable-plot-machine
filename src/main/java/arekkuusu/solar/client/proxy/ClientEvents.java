/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.proxy;

import arekkuusu.solar.api.helper.RayTraceHelper;
import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public void renderGhostAngstrom(RenderWorldLastEvent event) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItemMainhand();
		if(stack.isEmpty()) stack = player.getHeldItemOffhand();
		if(!stack.isEmpty() && stack.getItem() == ModItems.ANGSTROM) {
			RayTraceResult result = RayTraceHelper.tracePlayerHighlight(player);
			if(result.typeOfHit != RayTraceResult.Type.BLOCK) {
				Vector3 vec = Vector3.create(player.posX, player.posY + player.getEyeHeight(), player.posZ);
				vec.add(Vector3.create(player.getLookVec()).multiply(2.5D));
				BlockPos pos = new BlockPos(vec.toVec3d());
				IBlockState replaced = player.world.getBlockState(pos);
				if(player.world.isAirBlock(pos) || replaced.getBlock().isReplaceable(player.world, pos)) {
					GlStateManager.pushMatrix();
					GlStateManager.enableBlend();
					GlStateManager.enableAlpha();
					GlStateManager.color(1F, 1F, 1F, 0.2F);
					Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					IBlockState state = Block.getBlockFromItem(stack.getItem()).getDefaultState();

					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buff = tessellator.getBuffer();
					Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
					assert entity != null;
					double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.getPartialTicks();
					double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks();
					double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.getPartialTicks();
					buff.setTranslation(-d0, -d1, -d2);
					buff.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
					BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
					renderer.getBlockModelRenderer().renderModel(player.world, renderer.getModelForState(state), state, pos, buff, false);
					tessellator.draw();
					buff.setTranslation(0, 0, 0);

					GlStateManager.disableAlpha();
					GlStateManager.disableBlend();
					GlStateManager.popMatrix();
				}
			}
		}
	}
}
