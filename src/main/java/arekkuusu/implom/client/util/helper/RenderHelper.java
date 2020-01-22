/*
 * Arekkuusu / solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.helper;

import arekkuusu.implom.client.util.ShaderLibrary;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
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
	public static float FLUID_OFFSET = 0.005f;

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

	public static void renderFluidCuboid(FluidStack fluid, double renderX, double renderY, double renderZ,
										 BlockPos lightPos, BlockPos from, BlockPos to,
										 double ymin, double ymax, float edgeSpacingOffset) {
		pre(renderX, renderY, renderZ);
		GlStateManager.translate(from.getX(), from.getY(), from.getZ());
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
		TextureAtlasSprite flowing = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());
		if(still == null)
			still = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		if(flowing == null)
			flowing = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();

		boolean upsideDown = fluid.getFluid().isGaseous(fluid);

		int brightness = Minecraft.getMinecraft().world.getCombinedLight(lightPos, fluid.getFluid().getLuminosity());
		int l1 = brightness >> 0x10 & 0xFFFF;
		int l2 = brightness & 0xFFFF;

		int color = fluid.getFluid().getColor(fluid);
		int a = color >> 24 & 0xFF;
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;

		int yminInt = (int) ymin;

		int xd = to.getX() - from.getX();
		int yd = (int) (ymax - yminInt);
		int zd = to.getZ() - from.getZ();

		if(ymax % 1D == 0) yd--;

		double[] xs = new double[2 + xd];
		double[] ys = new double[2 + yd];
		double[] zs = new double[2 + zd];

		double xmin = edgeSpacingOffset;
		double xmax = xd + 1D - edgeSpacingOffset;
		double zmin = edgeSpacingOffset;
		double zmax = zd + 1D - edgeSpacingOffset;

		xs[0] = xmin;
		for(int i = 1; i <= xd; i++) xs[i] = i;
		xs[xd+1] = xmax;

		ys[0] = ymin;
		for(int i = 1; i <= yd; i++) ys[i] = i + ymin;
		ys[yd+1] = ymax;

		zs[0] = zmin;
		for(int i = 1; i <= zd; i++) zs[i] = i;
		zs[zd+1] = zmax;

		for(int y = 0; y <= yd; y++) {
			for(int z = 0; z <= zd; z++) {
				for(int x = 0; x <= xd; x++) {
					double x1 = xs[x];
					double y1 = ys[y];
					double z1 = zs[z];
					double w = xs[x + 1] - x1;
					double h = ys[y + 1] - y1;
					double d = zs[z + 1] - z1;

					if(x == 0) addSideQuadTexture(renderer, flowing, EnumFacing.WEST, x1, y1, z1, w, h, d, r, g, b, a, l1, l2, upsideDown, false);
					if(x == xd) addSideQuadTexture(renderer, flowing, EnumFacing.EAST, x1, y1, z1, w, h, d, r, g, b, a, l1, l2, upsideDown, false);
					if(y == 0) addSideQuadTexture(renderer, still, EnumFacing.DOWN, x1, y1, z1, w, h, d, r, g, b, a, l1, l2, upsideDown, false);
					if(y == yd) addSideQuadTexture(renderer, still, EnumFacing.UP, x1, y1, z1, w, h, d, r, g, b, a, l1, l2, upsideDown, false);
					if(z == 0) addSideQuadTexture(renderer, flowing, EnumFacing.NORTH, x1, y1, z1, w, h, d, r, g, b, a, l1, l2, upsideDown, false);
					if(z == zd) addSideQuadTexture(renderer, flowing, EnumFacing.SOUTH, x1, y1, z1, w, h, d, r, g, b, a, l1, l2, upsideDown, false);
				}
			}
		}

		tessellator.draw();
		post();
	}

	public static void addSideQuadTexture(BufferBuilder buff, TextureAtlasSprite sprite, EnumFacing face,
										  double x, double y, double z,
										  double w, double h, double d,
										  int r, int g, int b, int a,
										  int light1, int light2,
										  boolean flipHorizontally,
										  boolean flipVertical) {
		//Render Start & End
		double x1 = x;
		double y1 = y;
		double z1 = z;
		double x2 = x1 + w;
		double y2 = y1 + h;
		double z2 = z1 + d;

		//Sprite UV
		double minU;
		double maxU;
		double minV;
		double maxV;
		double sizeW = sprite.getIconWidth();
		double sizeH = sprite.getIconHeight();
		if(sizeW > 16F) {
			sizeW = (sizeW % 16F) / 16F;
		}
		if(sizeH > 16F) {
			sizeH = (sizeH % 16F) / 16F;
		}

		//Sprite dimensions
		double xSprite1 = x1 % 1D;
		double ySprite1 = y1 % 1D;
		double zSprite1 = z1 % 1D;

		double xSprite2 = xSprite1 + w;
		while(xSprite2 > 1F) xSprite2 -= 1F; //Cut the extra fat
		double ySprite2 = ySprite1 + h;
		while(ySprite2 > 1F) ySprite2 -= 1F; //Say no to donuts
		double zSprite2 = zSprite1 + d;
		while(zSprite2 > 1F) zSprite2 -= 1F; //Unless...

		if(flipVertical) {
			double tmp = 1D - ySprite1;
			ySprite1 = 1D - ySprite2;
			ySprite2 = tmp;
		}

		switch(face) {
			case DOWN:
			case UP:
				minU = sprite.getInterpolatedU(xSprite1 * sizeW);
				maxU = sprite.getInterpolatedU(xSprite2 * sizeW);
				minV = sprite.getInterpolatedV(zSprite1 * sizeH);
				maxV = sprite.getInterpolatedV(zSprite2 * sizeH);
				break;
			case NORTH:
			case SOUTH:
				minU = sprite.getInterpolatedU(xSprite2 * sizeW);
				maxU = sprite.getInterpolatedU(xSprite1 * sizeW);
				minV = sprite.getInterpolatedV(ySprite1 * sizeH);
				maxV = sprite.getInterpolatedV(ySprite2 * sizeH);
				break;
			case WEST:
			case EAST:
				minU = sprite.getInterpolatedU(zSprite2 * sizeW);
				maxU = sprite.getInterpolatedU(zSprite1 * sizeW);
				minV = sprite.getInterpolatedV(ySprite1 * sizeH);
				maxV = sprite.getInterpolatedV(ySprite2 * sizeH);
				break;
			default:
				minU = sprite.getMinU();
				maxU = sprite.getMaxU();
				minV = sprite.getMinV();
				maxV = sprite.getMaxV();
		}

		if(flipHorizontally) {
			double tmp = minV;
			minV = maxV;
			maxV = tmp;
		}

		switch(face) {
			case DOWN:
				buff.pos(x1, y1, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				break;
			case UP:
				buff.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x1, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				break;
			case NORTH:
				buff.pos(x1, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				break;
			case SOUTH:
				buff.pos(x1, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x1, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				break;
			case WEST:
				buff.pos(x1, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x1, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x1, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				break;
			case EAST:
				buff.pos(x2, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
				buff.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
				break;
		}
	}

	public static void pre(double x, double y, double z) {
		GlStateManager.pushMatrix();

		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(Minecraft.isAmbientOcclusionEnabled()) {
			GL11.glShadeModel(GL11.GL_SMOOTH);
		}
		else {
			GL11.glShadeModel(GL11.GL_FLAT);
		}

		GlStateManager.translate(x, y, z);
	}

	public static void post() {
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
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
		GlStateManager.translate(0, getInterpolated(tick, max, speed), 0);
	}

	public static double getInterpolated(float tick, float max, float speed) {
		float angle = 0;
		double toDegrees = Math.PI / 180D;
		angle += speed * tick;
		if(angle > 360) angle -= 360;
		return max * Math.sin(angle * toDegrees);
	}
}
