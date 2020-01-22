package arekkuusu.implom.client.util.baker;

import arekkuusu.implom.common.IPM;
import com.google.common.collect.Sets;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BakerBlock {

	public final ResourceLocation location;
	private Set<EnumFacing> ignoredFacings;
	private List<BakedQuad> quads;
	private IModel model;

	public BakerBlock(ResourceLocation location) {
		this.ignoredFacings = Sets.newHashSet(EnumFacing.DOWN);
		this.location = location;
	}

	public void loadModel() {
		try {
			model = ModelLoaderRegistry.getModel(location);
		} catch (Exception e) {
			IPM.LOG.fatal("[Baker Bakery] Failed to get json model: " + location.toString());
			e.printStackTrace();
		}
	}

	public void bake() {
		IBakedModel baked = model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
		quads = baked.getQuads(null, null, 0);
	}

	public void renderWithRotation(EnumFacing facing) {
		renderWithRotation(facing, EnumFacing.Axis.Y, 0);
	}

	public void renderWithRotation(EnumFacing facing, EnumFacing.Axis axis, double angle) {
		renderWithRotationYOffset(facing, axis, angle, 0);
	}

	public void renderWithYOffset(EnumFacing facing, double yOffset) {
		renderWithRotationYOffset(facing, EnumFacing.Axis.Y, 0, yOffset);
	}

	public void renderWithRotationYOffset(EnumFacing facing, EnumFacing.Axis axis, double angle, double yOffset) {
		GlStateManager.pushMatrix();
		if(!ignoredFacings.contains(facing)) {
			GlStateManager.translate(0.5, 0.5, 0.5);
			switch (facing) {
				case UP:
				case DOWN:
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					break;
				case NORTH:
					GlStateManager.rotate(90F, 1F, 0F, 0F);
					break;
				case SOUTH:
					GlStateManager.rotate(90F, -1F, 0F, 0F);
					break;
				case WEST:
					GlStateManager.rotate(90F, 0F, 0F, -1F);
					break;
				case EAST:
					GlStateManager.rotate(90F, 0F, 0F, 1F);
					break;
			}
			GlStateManager.translate(-0.5, -0.5, -0.5);
		}
		if(yOffset != 0)
			GlStateManager.translate(0, yOffset, 0);
		if(angle != 0) {
			GlStateManager.translate(0.5, 0.5, 0.5);
			GlStateManager.rotate(Quat.fromAxisAngle(axis, angle).toQuaternion());
			GlStateManager.translate(-0.5, -0.5, -0.5);
		}
		render();
		GlStateManager.popMatrix();
	}

	public void render() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
		for(BakedQuad bakedquad : quads) {
			LightUtil.renderQuadColor(buffer, bakedquad, -1);
		}
		tessellator.draw();
	}

	public BakerBlock withRotationsExcept(EnumFacing... ignoredFacings) {
		this.ignoredFacings = Sets.newHashSet(ignoredFacings);
		return this;
	}

	public List<BakedQuad> getQuads() {
		return quads;
	}

	public Collection<ResourceLocation> getTextures() {
		if(model == null) {
			IPM.LOG.fatal("[Baker Bakery] Model not yet loaded: " + location.toString());
		}
		return model.getTextures();
	}
}
