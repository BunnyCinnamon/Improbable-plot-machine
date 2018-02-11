/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.util.Vector3;
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
		super(world, pos, speed, rgb);
		this.particleMaxAge = age;
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
		this.prevPosX = posX += motionX;
		this.prevPosY = posY += motionY;
		this.prevPosZ = posZ += motionZ;
		move(motionX, motionY, motionZ);
	}

	@Override
	public int getBrightnessForRender(float idk) {
		return 255;
	}
}
