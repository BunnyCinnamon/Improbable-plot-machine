/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.entity;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.GLHelper;
import arekkuusu.solar.common.entity.EntityEyeOfSchrodinger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 04/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class EyeOfSchrodingerRenderer extends RenderLiving<EntityEyeOfSchrodinger> {

	public EyeOfSchrodingerRenderer(RenderManager manager) {
		super(manager, new EyeOfSchrodinger(), 0.25F);
		addLayer(new BrightLayer(this));
	}

	@Override
	public void doRender(EntityEyeOfSchrodinger schrodinger, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(schrodinger, x, y + 0.25, z, entityYaw, partialTicks);
	}

	@Override
	protected void applyRotations(EntityEyeOfSchrodinger schrodinger, float p_77043_2_, float rotationYaw, float partialTicks) {
		GlStateManager.rotate(180F - rotationYaw, 0F, 1F, 0F);
		GlStateManager.rotate(-schrodinger.rotationPitch, 1F, 0F, 0.0F);

		if(schrodinger.deathTime > 0) {
			float f = ((float) schrodinger.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);

			if(f > 1.0F) {
				f = 1.0F;
			}

			GlStateManager.rotate(f * this.getDeathMaxRotation(schrodinger), 0.0F, 0.0F, 1.0F);
		}
	}

	@Override
	protected float getDeathMaxRotation(EntityEyeOfSchrodinger entityLivingBaseIn) {
		return 360F;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityEyeOfSchrodinger entity) {
		return ResourceLibrary.EYE_OF_SCHRODINGER;
	}

	private static class BrightLayer implements LayerRenderer<EntityEyeOfSchrodinger> {

		private final EyeOfSchrodingerRenderer render;

		private BrightLayer(EyeOfSchrodingerRenderer render) {
			this.render = render;
		}

		@Override
		public void doRenderLayer(EntityEyeOfSchrodinger schrodinger, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			SpriteLibrary.EYE_OF_SCHRODINGER_LAYER.bindManager();

			if(schrodinger.isInvisible()) {
				GlStateManager.depthMask(false);
			} else {
				GlStateManager.depthMask(true);
			}

			boolean hasTarget = schrodinger.hasTargetedEntity();
			int rgb = hasTarget ? EntityEyeOfSchrodinger.RED : EntityEyeOfSchrodinger.BLUE;

			float r = (rgb >>> 16 & 0xFF) / 256.0F;
			float g = (rgb >>> 8 & 0xFF) / 256.0F;
			float b = (rgb & 0xFF) / 256.0F;
			GlStateManager.color(r, g, b, 1F);

			if(!hasTarget) {
				float brigthness = MathHelper.cos(schrodinger.ticksExisted * 0.05F);
				if(brigthness < 0) brigthness *= -1;
				brigthness *= 255F;
				GLHelper.lightMap(brigthness, brigthness);
			} else {
				GLHelper.lightMap(255F, 255F);
			}

			Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

			render.mainModel.render(schrodinger, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
			render.setLightmap(schrodinger);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
		}

		@Override
		public boolean shouldCombineTextures() {
			return false;
		}
	}

	private static class EyeOfSchrodinger extends ModelBase {

		final ModelRenderer top;
		final ModelRenderer bottom;
		final ModelRenderer right;
		final ModelRenderer left;

		EyeOfSchrodinger() {
			this.textureWidth = 32;
			this.textureHeight = 32;
			this.right = new ModelRenderer(this, 4, 12);
			this.right.setRotationPoint(0.0F, 24.0F, 0.0F);
			this.right.addBox(-4.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
			this.bottom = new ModelRenderer(this, 4, 6);
			this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
			this.bottom.addBox(-4.5F, 1.5F, -1.5F, 9, 3, 3, 0.0F);
			this.top = new ModelRenderer(this, 4, 0);
			this.top.setRotationPoint(0.0F, 24.0F, 0.0F);
			this.top.addBox(-4.5F, -4.5F, -1.5F, 9, 3, 3, 0.0F);
			this.left = new ModelRenderer(this, 16, 12);
			this.left.setRotationPoint(0.0F, 24.0F, 0.0F);
			this.left.addBox(1.5F, -1.5F, -1.5F, 3, 3, 3, 0.0F);
		}

		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			this.top.render(f5);
			this.left.render(f5);
			this.bottom.render(f5);
			this.right.render(f5);
		}
	}
}
