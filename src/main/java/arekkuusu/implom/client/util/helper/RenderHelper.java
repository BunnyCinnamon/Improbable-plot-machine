/*
 * Arekkuusu / solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.helper;

import arekkuusu.implom.api.helper.MathHelper;
import arekkuusu.implom.client.util.ShaderLibrary;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/*
 * Created by <Arekkuusu> on 30/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("SameParameterValue")
@SideOnly(Side.CLIENT)
public final class RenderHelper {

	private static final Random BEAM_RAND = new Random();

	public static float getRenderWorldTime(float partialTicks) {
		return (Minecraft.getSystemTime() + partialTicks) / 20F;
	}

	public static float getRenderPlayerTime() {
		return Minecraft.getMinecraft().player.ticksExisted;
	}

	public static void renderBeams(float age, int number, int startRBG, int endRGB, float size) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		BEAM_RAND.setSeed(432L);
		float r_ = (startRBG >>> 16 & 0xFF) / 256F;
		float g_ = (startRBG >>> 8 & 0xFF) / 256F;
		float b_ = (startRBG & 0xFF) / 256F;

		float r = (endRGB >>> 16 & 0xFF) / 256F;
		float g = (endRGB >>> 8 & 0xFF) / 256F;
		float b = (endRGB & 0xFF) / 256F;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		float rotation = age % 500;

		for(int i = 0; (float) i < number; ++i) {
			GlStateManager.rotate(BEAM_RAND.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(BEAM_RAND.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(BEAM_RAND.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(BEAM_RAND.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(BEAM_RAND.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(BEAM_RAND.nextFloat() * 360.0F + rotation * 90.0F, 0.0F, 0.0F, 1.0F);
			float min = (size * 0.5F);
			float resized = BEAM_RAND.nextFloat() * size + min;
			float sizeMulti = BEAM_RAND.nextFloat() * min + (min * 0.5F);
			bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(r_, g_, b_, 1F).endVertex();
			bufferbuilder.pos(-0.866D * sizeMulti, resized, (-0.5F * sizeMulti)).color(r, g, b, 0F).endVertex();
			bufferbuilder.pos(0.866D * sizeMulti, resized, (-0.5F * sizeMulti)).color(r, g, b, 0F).endVertex();
			bufferbuilder.pos(0.0D, resized, (1.0F * sizeMulti)).color(r, g, b, 0F).endVertex();
			bufferbuilder.pos(-0.866D * sizeMulti, resized, (-0.5F * sizeMulti)).color(r, g, b, 0F).endVertex();
			tessellator.draw();
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderItemStack(ItemStack stack) {
		//Fix stack 'y' center
		if(stack.getItem() instanceof ItemBlock) {
			GlStateManager.translate(0F, -0.1F, 0F);
		}
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableRescaleNormal();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		IBakedModel model = render.getItemModelWithOverrides(stack, null, null);
		IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
		render.renderItem(stack, transformedModel);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		GlStateManager.disableRescaleNormal();
	}

	public static void renderGhostBlock(BlockPos pos, IBlockState state) {
		Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		if(entity != null) {
			double x = Minecraft.getMinecraft().getRenderManager().viewerPosX;
			double y = Minecraft.getMinecraft().getRenderManager().viewerPosY;
			double z = Minecraft.getMinecraft().getRenderManager().viewerPosZ;
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			ShaderLibrary.ALPHA.begin();
			ShaderLibrary.ALPHA.getUniformJ("alpha").ifPresent(alpha -> {
				alpha.set(0.4F);
				alpha.upload();
			});
			Blending.Normal().apply();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.translate(-x, -y, -z);
			GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ() + 1);
			BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
			renderer.renderBlockBrightness(state, 1F);
			ShaderLibrary.ALPHA.end();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

	public static void renderSideTexture(EnumFacing facing, double uMin, double uMax, double vMin, double vMax) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		switch (facing) {
			case DOWN:
				//Bottom
				buff.pos(0, 0, 1).tex(uMax, vMin).endVertex();
				buff.pos(0, 0, 0).tex(uMin, vMax).endVertex();
				buff.pos(1, 0, 0).tex(uMin, vMax).endVertex();
				buff.pos(1, 0, 1).tex(uMax, vMin).endVertex();
				break;
			case UP:
				//Top
				buff.pos(0, 1, 0).tex(uMax, vMin).endVertex();
				buff.pos(1, 1, 0).tex(uMax, vMax).endVertex();
				buff.pos(1, 1, 1).tex(uMin, vMax).endVertex();
				buff.pos(0, 1, 1).tex(uMin, vMin).endVertex();
				break;
			case NORTH:
				//Front
				buff.pos(0, 0, 0).tex(uMin, vMin).endVertex();
				buff.pos(1, 0, 0).tex(uMax, vMin).endVertex();
				buff.pos(1, 1, 0).tex(uMax, vMax).endVertex();
				buff.pos(0, 1, 0).tex(uMin, vMax).endVertex();
				break;
			case SOUTH:
				//Back
				buff.pos(1, 0, 1).tex(uMin, vMin).endVertex();
				buff.pos(0, 0, 1).tex(uMax, vMin).endVertex();
				buff.pos(0, 1, 1).tex(uMax, vMax).endVertex();
				buff.pos(1, 1, 1).tex(uMin, vMax).endVertex();
				break;
			case WEST:
				buff.pos(0, 0, 1).tex(uMin, vMin).endVertex();
				buff.pos(0, 0, 0).tex(uMax, vMin).endVertex();
				buff.pos(0, 1, 0).tex(uMax, vMax).endVertex();
				buff.pos(0, 1, 1).tex(uMin, vMax).endVertex();
				//Right
				break;
			case EAST:
				//Left
				buff.pos(1, 0, 0).tex(uMin, vMin).endVertex();
				buff.pos(1, 0, 1).tex(uMax, vMin).endVertex();
				buff.pos(1, 1, 1).tex(uMax, vMax).endVertex();
				buff.pos(1, 1, 0).tex(uMin, vMax).endVertex();
				break;
		}
		tessellator.draw();
	}

	public static void makeUpDownTranslation(float tick, float max, float speed) {
		GlStateManager.translate(0, MathHelper.getInterpolated(tick, max, speed), 0);
	}
}
