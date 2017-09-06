/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.effect.ParticleBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <Arekkuusu> on 26/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class ParticleRenderer {

	private final List<ParticleBase> particles = new ArrayList<>();

	public void update() {
		for(int i = 0; i < particles.size(); i++) {
			Particle particle = particles.get(i);
			if(!particle.isAlive()) {
				particles.remove(i);
			} else {
				particle.onUpdate();
			}
		}
	}

	public void renderAll(float partial) {
		Entity entity = Minecraft.getMinecraft().player;
		if(entity != null) {
			Particle.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial;
			Particle.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial;
			Particle.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial;
			Particle.cameraViewDir = entity.getLook(partial);

			float f = ActiveRenderInfo.getRotationX();
			float f1 = ActiveRenderInfo.getRotationZ();
			float f2 = ActiveRenderInfo.getRotationYZ();
			float f3 = ActiveRenderInfo.getRotationXY();
			float f4 = ActiveRenderInfo.getRotationXZ();

			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.alphaFunc(516, 0.003921569F);
			GlStateManager.disableCull();
			GlStateManager.depthMask(false);

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder buffer = tess.getBuffer();

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			for(ParticleBase particle : particles) {
				if(particle.shouldRender() && !particle.isAdditive()) {
					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
					particle.renderParticle(buffer, entity, partial, f, f4, f1, f2, f3);
					tess.draw();
				}
			}

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			for(ParticleBase particle : particles) {
				if(particle.shouldRender() && particle.isAdditive()) {
					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
					particle.renderParticle(buffer, entity, partial, f, f4, f1, f2, f3);
					tess.draw();
				}
			}

			GlStateManager.disableDepth();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			for(ParticleBase particle : particles) {
				if(particle.shouldRender() && !particle.isAdditive() && particle.shouldDisableDepth()) {
					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
					particle.renderParticle(buffer, entity, partial, f, f4, f1, f2, f3);
					tess.draw();
				}
			}

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			for(ParticleBase particle : particles) {
				if(particle.shouldRender() && particle.isAdditive() && particle.shouldDisableDepth()) {
					buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
					particle.renderParticle(buffer, entity, partial, f, f4, f1, f2, f3);
					tess.draw();
				}
			}
			GlStateManager.enableDepth();

			GlStateManager.enableCull();
			GlStateManager.depthMask(true);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(516, 0.1F);
		}
	}

	public void add(ParticleBase particle) {
		particles.add(particle);
	}
}
