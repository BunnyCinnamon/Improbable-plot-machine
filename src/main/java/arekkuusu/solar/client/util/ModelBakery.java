/*******************************************************************************
 * Arekkuusu / solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.client.util;

import arekkuusu.solar.client.render.baked.model.Cube;
import arekkuusu.solar.common.Solar;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import java.util.Map;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 30/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ModelBakery {

	private static final Random BEAM_RAND = new Random();
	private static int sphere;
	private static int cube;

	public static void bake() {
		//--------------------Sphere--------------------//
		Sphere sphereForm = new Sphere();
		sphereForm.setDrawStyle(GLU.GLU_FILL);
		sphereForm.setNormals(GLU.GLU_FLAT);

		sphere = GL11.glGenLists(1);
		GL11.glNewList(sphere, GL11.GL_COMPILE);

		sphereForm.draw(1F, 16, 16);

		GL11.glEndList();
		//---------------------Cube---------------------//
		Cube cubeForm = new Cube();
		cubeForm.setDrawStyle(GLU.GLU_FILL);
		cubeForm.setNormals(GLU.GLU_FLAT);

		cube = GL11.glGenLists(1);
		GL11.glNewList(cube, GL11.GL_COMPILE);

		cubeForm.draw();

		GL11.glEndList();
		//------------------OBJ Models------------------//
		for(OBJBaker model : OBJBaker.values()) {
			try {
				model.bake();
			} catch(Exception e) {
				Solar.LOG.error("[Model Bakery] Failed to bake obj model: " + model.location.toString());
				e.printStackTrace();
			}
		}

		//-----------------Json Models-----------------//
		for(BlockBaker model : BlockBaker.values()) {
			try {
				model.bake();
			} catch(Exception e) {
				Solar.LOG.fatal("[Model Bakery] Failed to bake json model: " + model.location.toString());
				e.printStackTrace();
			}
		}
		Solar.LOG.info("[THE CAKE IS A LIE!]");
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
		buff.pos(size, -size, -size).tex(uMax, vMax).endVertex();
		buff.pos(size, -size, size).tex(uMin, vMax).endVertex();
		buff.pos(-size, -size, size).tex(uMin, vMin).endVertex();

		tessellator.draw();
	}

	public static void renderBeams(float age, int number, int startRBG, int endRGB, float size) {
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
	}

	public enum OBJBaker {
		;

		ResourceLocation location;
		IModel model;

		OBJBaker(String name) {
			location = ResourceLibrary.getAtlas(ResourceLibrary.ModelLocation.OBJ, name);
		}

		void bake() throws Exception {
			model = OBJLoader.INSTANCE.loadModel(location);
		}

		IBakedModel bake(ImmutableMap<String, String> map) {
			model.retexture(map);
			return model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
		}

		public static void render(OBJBaker model, Map<String, ResourceLocation> map) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

			ImmutableMap.Builder<String, String> immutable = ImmutableMap.builder();
			for(Map.Entry<String, ResourceLocation> entry : map.entrySet()) {
				immutable.put(entry.getKey(), entry.getValue().toString());
			}
			IBakedModel baked = model.bake(immutable.build());

			for(BakedQuad bakedquad : baked.getQuads(null, null, 0))
				LightUtil.renderQuadColor(buffer, bakedquad, -1);
			tessellator.draw();
		}
	}

	public enum BlockBaker {
		PRIMAL_SIDE("primal_side");

		ResourceLocation location;
		IBakedModel baked;

		BlockBaker(String name) {
			location = ResourceLibrary.getAtlas(ResourceLibrary.ModelLocation.BLOCK, name);
		}

		void bake() throws Exception {
			IModel model = ModelLoaderRegistry.getModel(location);
			baked = model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
		}

		public static void render(BlockBaker model) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

			for(BakedQuad bakedquad : model.baked.getQuads(null, null, 0)) {
				LightUtil.renderQuadColor(buffer, bakedquad, -1);
			}
			tessellator.draw();
		}
	}
}
