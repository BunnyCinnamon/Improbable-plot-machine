/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.util.resource.FrameSpriteResource;
import arekkuusu.solar.client.util.resource.SpriteResource;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 23/07/2017.
 * It's distributed as part of Solar.
 */
public class ParticleBase extends Particle {

	private SpriteResource sprite;

	ParticleBase(World world, Vector3 pos, Vector3 speed, int rgb) {
		super(world, pos.x, pos.y, pos.z, 0, 0, 0);
		this.motionX = speed.x;
		this.motionY = speed.y;
		this.motionZ = speed.z;
		float r = (rgb >>> 16 & 0xFF) / 256.0F;
		float g = (rgb >>> 8 & 0xFF) / 256.0F;
		float b = (rgb & 0xFF) / 256.0F;
		setRBGColorF(r, g, b);
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		if(sprite != null) {
			double uMin = 0F;
			double uMax = 1F;
			double vMin = 0F;
			double vMax = 1F;
			if(sprite instanceof FrameSpriteResource) {
				FrameSpriteResource framedSprite = ((FrameSpriteResource) sprite);
				Tuple<Double, Double> uv = framedSprite.getUVFrame((int) particleAngle);
				double uOffset = framedSprite.getU();
				double u = uv.getFirst();
				double vOffset = framedSprite.getV();
				double v = uv.getSecond();

				uMin = u;
				uMax = u + uOffset;
				vMin = v;
				vMax = v + vOffset;
			}

			sprite.bindManager();
			renderEasy(buffer, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ, uMin, uMax, vMin, vMax);
		}
	}

	/**
	 * Fix for particle wobbliness
	 */
	private void renderEasy(BufferBuilder buffer, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, double uMin, double uMax, double vMin, double vMax) {
		int light = getBrightnessForRender(partialTicks);
		float scale = 0.1F * particleScale;

		double x = prevPosX + (posX - prevPosX) * partialTicks - interpPosX;
		double y = prevPosY + (posY - prevPosY) * partialTicks - interpPosY;
		double z = prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ;
		Vec3d vec0 = new Vec3d(-rotationX * scale - rotationXY * scale, -rotationZ * scale, -rotationYZ * scale - rotationXZ * scale);
		Vec3d vec1 = new Vec3d(-rotationX * scale + rotationXY * scale, rotationZ * scale, -rotationYZ * scale + rotationXZ * scale);
		Vec3d vec2 = new Vec3d(rotationX * scale + rotationXY * scale, rotationZ * scale, rotationYZ * scale + rotationXZ * scale);
		Vec3d vec3 = new Vec3d(rotationX * scale - rotationXY * scale, -rotationZ * scale, rotationYZ * scale - rotationXZ * scale);

		if(this.particleAngle != 0.0F) {
			float life = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
			//Press F to pay respect
			float f = MathHelper.cos(life * 0.5F);
			float ff = MathHelper.sin(life * 0.5F) * (float) cameraViewDir.x;
			float fff = MathHelper.sin(life * 0.5F) * (float) cameraViewDir.y;
			float ffff = MathHelper.sin(life * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec = new Vec3d((double) ff, (double) fff, (double) ffff);

			vec0 = vec.scale(2.0D * vec0.dotProduct(vec)).add(vec0.scale((f * f) - vec.dotProduct(vec))).add(vec.crossProduct(vec0).scale(2.0F * f));
			vec1 = vec.scale(2.0D * vec1.dotProduct(vec)).add(vec1.scale((f * f) - vec.dotProduct(vec))).add(vec.crossProduct(vec1).scale(2.0F * f));
			vec2 = vec.scale(2.0D * vec2.dotProduct(vec)).add(vec2.scale((f * f) - vec.dotProduct(vec))).add(vec.crossProduct(vec2).scale(2.0F * f));
			vec3 = vec.scale(2.0D * vec3.dotProduct(vec)).add(vec3.scale((f * f) - vec.dotProduct(vec))).add(vec.crossProduct(vec3).scale(2.0F * f));
		}

		buffer.pos(x + vec0.x, y + vec0.y, z + vec0.z).tex(uMax, vMax).color(getRedColorF(), getGreenColorF(), getBlueColorF(), particleAlpha).lightmap(light, light).endVertex();
		buffer.pos(x + vec1.x, y + vec1.y, z + vec1.z).tex(uMax, vMin).color(getRedColorF(), getGreenColorF(), getBlueColorF(), particleAlpha).lightmap(light, light).endVertex();
		buffer.pos(x + vec2.x, y + vec2.y, z + vec2.z).tex(uMin, vMin).color(getRedColorF(), getGreenColorF(), getBlueColorF(), particleAlpha).lightmap(light, light).endVertex();
		buffer.pos(x + vec3.x, y + vec3.y, z + vec3.z).tex(uMin, vMax).color(getRedColorF(), getGreenColorF(), getBlueColorF(), particleAlpha).lightmap(light, light).endVertex();
	}

	public void setSprite(SpriteResource sprite) {
		this.sprite = sprite;
	}

	@Override
	public int getFXLayer() {
		return 3;
	}

	public boolean shouldRender() {
		return true;
	}

	public boolean isAdditive() {
		return true;
	}
}
