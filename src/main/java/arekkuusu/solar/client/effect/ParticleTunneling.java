/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import net.katsstuff.mirror.client.particles.GlowTexture;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 04/08/2017.
 * It's distributed as part of Solar.
 */
public class ParticleTunneling extends ParticleMute {

	ParticleTunneling(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		super(world, pos, speed, scale, age, rgb, glow);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(rand.nextInt(6) == 0) {
			this.particleAge++;
		}
		float life = (float) this.particleAge / (float) this.particleMaxAge;
		this.particleScale = initScale - initScale * life;
		this.particleAlpha = 0.5F * (1.0f - life);
		this.prevParticleAngle = particleAngle;
		this.particleAngle += 1.0f;
	}

	@Override
	public boolean shouldDisableDepth() {
		return true;
	}
}
