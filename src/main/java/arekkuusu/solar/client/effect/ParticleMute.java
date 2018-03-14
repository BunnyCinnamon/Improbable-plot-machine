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
 * Created by <Arekkuusu> on 23/07/2017.
 * It's distributed as part of Solar.
 */
public class ParticleMute extends ParticleBase {

	private final int rgb;

	ParticleMute(World world, Vector3 pos, Vector3 speed, float scale, int age, int rgb, GlowTexture glow) {
		super(world, pos, speed, scale, age, rgb);
		setParticleTexture(glow.getTexture());
		this.rgb = rgb;
	}

	@Override
	public boolean isAdditive() {
		return rgb > 0x000000;
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		return isAdditive() ? 255 : 0;
	}
}
