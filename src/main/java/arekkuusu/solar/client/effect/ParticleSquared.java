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
 * Created by <Snack> on 06/01/2018.
 * It's distributed as part of Solar.
 */
public class ParticleSquared extends ParticleBase {

	private final float initScale;

	ParticleSquared(World world, Vector3 pos, Vector3 speed, int rgb, float scale, int age) {
		super(world, pos, speed, rgb);
		this.particleMaxAge = age;
		this.particleScale = scale;
		this.initScale = particleScale;
		setSprite(SpriteLibrary.SQUARED);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		float life = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleScale = initScale - initScale * life;
		this.particleAlpha = 0.5F * (1.0f - life);
		if(particleAlpha <= 0.1) setExpired();
	}

	@Override
	public boolean shouldRender() {
		return particleAlpha > 0.1;
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		return 255;
	}
}
