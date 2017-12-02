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

	ParticleQuorn(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {
		super(world, pos.x, pos.y, pos.z, 0, 0, 0);
		float r = (rgb >>> 16 & 0xFF) / 256.0F;
		float g = (rgb >>> 8 & 0xFF) / 256.0F;
		float b = (rgb & 0xFF) / 256.0F;
		setRBGColorF(r, g, b);

		this.particleMaxAge = age;
		this.particleScale = scale;
		this.canCollide = false;

		motionX = speed.x;
		motionY = speed.y;
		motionZ = speed.z;

		setSprite(SpriteLibrary.QUORN_PARTICLE);
	}

	@Override
	public void onUpdate() {
		if(particleAge++ >= particleMaxAge) {
			setExpired();
		}
		float life = (float) particleAge / (float) particleMaxAge;
		this.particleAlpha = 1F * (1F - life);

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
