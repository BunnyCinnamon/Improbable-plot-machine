/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.helper;

import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.client.util.baker.model.Cube;
import arekkuusu.solar.common.Solar;
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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Quadric;
import org.lwjgl.util.glu.Sphere;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by <Arekkuusu> on 30/06/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("SameParameterValue")
@SideOnly(Side.CLIENT)
public final class RenderHelper {

	private static final Random BEAM_RAND = new Random();
	private static int sphere;
	private static int cube;

	public static void bake() {
		//--------------------Sphere--------------------//
		sphere = addDraw(new Sphere(), GLU.GLU_FILL, GLU.GLU_FLAT, form -> form.draw(1F, 16, 16));

		//---------------------Cube---------------------//
		cube = addDraw(new Cube(), GLU.GLU_FILL, GLU.GLU_FLAT, Cube::draw);

		//-----------------Json Models-----------------//
		BlockBaker.bakeAll();
		Solar.LOG.info("[PIE HAS BEEN SUCCESSFULLY BAKED!]");
	}

	private static <T extends Quadric> int addDraw(T form, int draw, int normal, Consumer<T> consumer) {
		form.setDrawStyle(draw);
		form.setNormals(normal);
		int id = GL11.glGenLists(1);

		GL11.glNewList(id, GL11.GL_COMPILE);
		consumer.accept(form);
		GL11.glEndList();

		return id;
	}

	public static void drawSphere(int color, float alpha) {
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;

		GlStateManager.color(r, g, b, alpha);
		GL11.glCallList(sphere);
	}

	public static void drawCube(int color, float alpha) {
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;

		GlStateManager.color(r, g, b, alpha);
		GL11.glCallList(cube);
	}

	public static float getRenderWorldTime(float partialTicks) {
		return (Minecraft.getSystemTime() + partialTicks) / 20F;
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
			bufferbuilder.pos(-0.866D *  sizeMulti,  resized,  (-0.5F * sizeMulti)).color(r, g, b, 0F).endVertex();
			bufferbuilder.pos(0.866D *  sizeMulti,  resized,  (-0.5F * sizeMulti)).color(r, g, b, 0F).endVertex();
			bufferbuilder.pos(0.0D,  resized,  (1.0F * sizeMulti)).color(r, g, b, 0F).endVertex();
			bufferbuilder.pos(-0.866D *  sizeMulti,  resized,  (-0.5F * sizeMulti)).color(r, g, b, 0F).endVertex();
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
		net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		IBakedModel model = render.getItemModelWithOverrides(stack, null, null);
		IBakedModel transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);
		render.renderItem(stack, transformedModel);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
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
			GLHelper.BLEND_SRC_ALPHA$ONE_MINUS_SRC_ALPHA.blend();
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

	public static void makeUpDownTranslation(float tick, float max, float speed, float angle) {
		float toDegrees = (float) Math.PI / 180F;
		angle += speed * tick;
		if (angle > 360) angle -= 360;

		GlStateManager.translate(0, max * Math.sin(angle * toDegrees), 0);
	}
}
