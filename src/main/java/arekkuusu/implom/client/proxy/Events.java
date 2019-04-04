/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.proxy;

import arekkuusu.implom.api.capability.data.WorldAccessNBTData;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.api.helper.RayTraceHelper;
import arekkuusu.implom.api.capability.WorldAccessHelper;
import arekkuusu.implom.client.util.SpriteLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.client.util.sprite.UVFrame;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.block.tile.TileMutator;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibMod;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/*
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
@EventBusSubscriber(modid = LibMod.MOD_ID, value = Side.CLIENT)
public class Events {

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
				Vector3 vec = Vector3.apply(player.posX, player.posY + player.getEyeHeight(), player.posZ)
						.add(new Vector3(player.getLookVec()).multiply(2.5D));
				BlockPos pos = new BlockPos(vec.toVec3d());
				IBlockState replaced = player.world.getBlockState(pos);
				if(player.world.isAirBlock(pos) || replaced.getBlock().isReplaceable(player.world, pos)) {
					RenderHelper.renderGhostBlock(pos, ModBlocks.ANGSTROM.getDefaultState());
				}
			}
		}
	}

	@SubscribeEvent
	public static void renderMutatorWorld(DrawBlockHighlightEvent event) {
		if(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = event.getTarget().getBlockPos();
			EntityPlayer player = event.getPlayer();
			IBlockState state = player.world.getBlockState(pos);
			if(state.getBlock() == ModBlocks.MUTATOR) {
				TileMutator tile = (TileMutator) player.world.getTileEntity(pos);
				if(tile == null) return;
				WorldAccessHelper.getCapability(tile, tile.getFacingLazy().getOpposite()).map(IWorldAccessNBTDataCapability::get).ifPresent(Events::renderMutator);
			}
		}
	}

	@SubscribeEvent
	public static void renderMutatorInventory(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItemMainhand();
		if(stack.isEmpty()) stack = player.getHeldItemOffhand();
		if(stack.getItem() == ModItems.MUTATOR) {
			GlStateManager.enableBlend();
			GlStateManager.disableCull();
			WorldAccessHelper.getCapability(stack).map(IWorldAccessNBTDataCapability::get).ifPresent(Events::renderMutator);
			GlStateManager.enableCull();
			GlStateManager.disableBlend();
		}
	}

	private static void renderMutator(WorldAccessNBTData data) {
		if(data.getWorld() != null && data.getPos() != null && data.getFacing() != null
				&& data.getWorld().provider.getDimension() == Minecraft.getMinecraft().player.world.provider.getDimension()) {
			double x = Minecraft.getMinecraft().getRenderManager().viewerPosX;
			double y = Minecraft.getMinecraft().getRenderManager().viewerPosY;
			double z = Minecraft.getMinecraft().getRenderManager().viewerPosZ;
			GlStateManager.pushMatrix();
			GlStateManager.depthMask(false);
			Blending.Normal().apply();
			GlStateManager.translate(-x, -y, -z);
			GlStateManager.translate(data.getPos().getX(), data.getPos().getY(), data.getPos().getZ());
			SpriteLibrary.MUTATOR_SELECTION.bind();
			UVFrame frame = SpriteLibrary.MUTATOR_SELECTION.getFrame();
			RenderHelper.renderSideTexture(data.getFacing(), frame.uMin, frame.uMax, frame.vMin, frame.vMax);
			GlStateManager.depthMask(true);
			GlStateManager.popMatrix();
		}
	}
}
