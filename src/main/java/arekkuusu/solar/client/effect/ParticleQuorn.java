/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.util.SpriteLibrary;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 23/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class ParticleQuorn extends ParticleBase {

	private final Vector3 point;
	private final double speed;

	ParticleQuorn(World world, Vector3 from, double speed, Vector3 to, float scale, int rgb) {
		super(world, from.x, from.y, from.z, 0, 0, 0);
		float r = (rgb >>> 16 & 0xFF) / 256.0F;
		float g = (rgb >>> 8 & 0xFF) / 256.0F;
		float b = (rgb & 0xFF) / 256.0F;
		setRBGColorF(r, g, b);

		this.point = from;
		this.speed = speed;

		double distance = from.distanceTo(to);

		this.particleMaxAge = (int) ((distance / speed) * 0.5);
		this.particleScale = scale;
		this.canCollide = false;

		setSprite(SpriteLibrary.QUORN_PARTICLE);
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
