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
public class ParticleDark extends ParticleBase {

	private final float initScale;

	ParticleDark(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale) {
		super(world, pos, speed, rgb);
		particleMaxAge = age;
		particleScale = scale;
		initScale = particleScale;
		particleAngle = rand.nextBoolean() ? 2F : -2F * (float) Math.PI;
		canCollide = false;

		setSprite(SpriteLibrary.DARK_PARTICLE);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(rand.nextInt(6) == 0) {
			particleAge++;
		}
		float life = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleScale = initScale - initScale * life;
		this.particleAlpha = 0.5F * (1.0f - life);
		this.prevParticleAngle = particleAngle;
		particleAngle += 1.0f;
	}

	@Override
	public boolean isAdditive() {
		return false;
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		return 0;
	}
}
