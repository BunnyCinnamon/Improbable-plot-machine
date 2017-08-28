/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.client.util.SpriteLibrary;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 23/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class ParticleQuorn extends ParticleBase {

	private final Vec3d point;
	private final double speed;

	ParticleQuorn(World world, double xCoord, double yCoord, double zCoord, double speed, double xPoint, double yPoint, double zPoint, float scale, int rgb) {
		super(world, xCoord, yCoord, zCoord, 0, 0, 0);
		float r = (rgb >>> 16 & 0xFF) / 256.0F;
		float g = (rgb >>> 8 & 0xFF) / 256.0F;
		float b = (rgb & 0xFF) / 256.0F;
		setRBGColorF(r, g, b);

		this.point = new Vec3d(xPoint, yPoint, zPoint);
		this.speed = speed;

		double x = posX - point.x;
		double y = posY - point.y;
		double z = posZ - point.z;

		double square = x * x + y * y + z * z;
		double distance = Math.sqrt(square);

		particleMaxAge = (int) ((distance / speed) * 0.5);
		particleScale = scale;
		canCollide = false;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		SpriteLibrary.QUORN_PARTICLE.bindManager();
		Tuple<Double, Double> uv = SpriteLibrary.QUORN_PARTICLE.getUVFrame(particleAge);
		double vOffset = SpriteLibrary.QUORN_PARTICLE.getV();
		double v = uv.getSecond();

		renderEasy(buffer, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ, 0, 1, v, v + vOffset);
	}

	@Override
	public void onUpdate() {
		if(particleAge++ >= particleMaxAge) {
			setExpired();
		}
		float life = (float) particleAge / (float) particleMaxAge;
		this.particleAlpha = 1F * (1F - life);

		double x = posX - point.x;
		double y = posY - point.y;
		double z = posZ - point.z;

		double square = x * x + y * y + z * z;
		double distance = Math.sqrt(square);

		motionX = speed * (x / distance);
		motionY = speed * (y / distance);
		motionZ = speed * (z / distance);

		prevPosX = posX += motionX;
		prevPosY = posY += motionY;
		prevPosZ = posZ += motionZ;

		move(motionX, motionY, motionZ);
	}

	@Override
	public int getBrightnessForRender(float idk) {
		return 255;
	}
}
