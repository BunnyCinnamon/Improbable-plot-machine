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

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
public class ParticleLight extends ParticleBase {

	private final float initScale;
	private final boolean dark;

	ParticleLight(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale) {
		super(world, pos, speed, rgb);
		this.particleMaxAge = age;
		this.particleScale = scale;
		this.initScale = particleScale;
		this.particleAngle = rand.nextBoolean() ? 2F : -2F * (float) Math.PI;
		this.canCollide = false;
		this.dark = rgb == 0x000000;
		setSprite(dark ? SpriteLibrary.DARK_PARTICLE : SpriteLibrary.LIGHT_PARTICLE);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(rand.nextInt(2) == 0) {
			this.particleAge++;
		}
		float life = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleScale = initScale - initScale * life;
		this.particleAlpha = 0.5F * (1.0f - life);
		this.prevParticleAngle = particleAngle;
		this.particleAngle += 1.0f;
	}

	@Override
	public int getBrightnessForRender(float idk) {
		return dark ? 0 : 255;
	}

	@Override
	public boolean isAdditive() {
		return !dark;
	}
}
