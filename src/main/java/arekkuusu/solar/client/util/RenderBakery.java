/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.render.baked.model.Cube;
import arekkuusu.solar.client.util.helper.BlockBaker;
import arekkuusu.solar.common.Solar;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
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
public final class RenderBakery {

	private static final Random BEAM_RAND = new Random();
	private static int sphere;
	private static int cube;

	public static void bake() {
		//--------------------Sphere--------------------//
		sphere = addDraw(new Sphere(), GLU.GLU_FILL, GLU.GLU_FLAT, form -> form.draw(1F, 16, 16));

		//---------------------Cube---------------------//
		cube = addDraw(new Cube(), GLU.GLU_FILL, GLU.GLU_FLAT, Cube::draw);

		//-----------------Json Models-----------------//
		for(BlockBaker model : BlockBaker.values()) {
			try {
				model.bake();
			} catch(Exception e) {
				Solar.LOG.fatal("[Model Bakery] Failed to bake json model: " + model.getLocation().toString());
				e.printStackTrace();
			}
		}
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

	public static void renderCube(float size, double uMin, double uMax, double vMin, double vMax) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		//Front
		buff.pos(size, -size, -size).tex(uMax, vMin).endVertex();
		buff.pos(size, size, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, size, -size).tex(uMin, vMax).endVertex();
		buff.pos(-size, -size, -size).tex(uMin, vMin).endVertex();
		//Back
		buff.pos(-size, -size, size).tex(uMax, vMin).endVertex();
		buff.pos(-size, size, size).tex(uMax, vMax).endVertex();
		buff.pos(size, size, size).tex(uMin, vMax).endVertex();
		buff.pos(size, -size, size).tex(uMin, vMin).endVertex();
		//Right
		buff.pos(size, -size, size).tex(uMax, vMin).endVertex();
		buff.pos(size, size, size).tex(uMax, vMax).endVertex();
		buff.pos(size, size, -size).tex(uMin, vMax).endVertex();
		buff.pos(size, -size, -size).tex(uMin, vMin).endVertex();
		//Left
		buff.pos(-size, -size, -size).tex(uMax, vMin).endVertex();
		buff.pos(-size, size, -size).tex(uMax, vMax).endVertex();
		buff.pos(-size, size, size).tex(uMin, vMax).endVertex();
		buff.pos(-size, -size, size).tex(uMin, vMin).endVertex();
		//Top
		buff.pos(-size, size, -size).tex(uMax, vMin).endVertex();
		buff.pos(size, size, -size).tex(uMax, vMax).endVertex();
		buff.pos(size, size, size).tex(uMin, vMax).endVertex();
		buff.pos(-size, size, size).tex(uMin, vMin).endVertex();
		//Bottom
		buff.pos(-size, -size, -size).tex(uMax, vMin).endVertex();
		buff.pos(-size, -size, size).tex(uMin, vMin).endVertex();
		buff.pos(size, -size, size).tex(uMin, vMax).endVertex();
		buff.pos(size, -size, -size).tex(uMax, vMax).endVertex();

		tessellator.draw();
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
		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		render.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
	}

	public static void renderItemBlock(BlockPos pos, Item item, double partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		IBlockState state = Block.getBlockFromItem(item).getDefaultState();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		assert entity != null;
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
		double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
		buff.setTranslation(-d0, -d1, -d2);
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		BlockRendererDispatcher renderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		renderer.getBlockModelRenderer().renderModel(entity.world, renderer.getModelForState(state), state, pos, buff, false);
		tessellator.draw();
		buff.setTranslation(0, 0, 0);

		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void makeUpDownTranslation(float tick, float max, float speed, float angle) {
		float toDegrees = (float) Math.PI / 180F;
		angle += speed * tick;
		if (angle > 360) angle -= 360;

		GlStateManager.translate(0, max * Math.sin(angle * toDegrees), 0);
	}
}
