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
 * Created by <Arekkuusu> on 4/27/2018.
 * It's distributed as part of Solar.
 */
public class ParticleLuminescence extends ParticleBase {

	private final float initialD = 90; //get it?
	private final float initialG;
	private final float initialB;

	ParticleLuminescence(World world, Vector3 pos, Vector3 speed, float scale, int age, GlowTexture glow) {
		super(world, pos, speed, scale, age, 0xFFE077);
		setParticleTexture(glow.getTexture());
		this.initialG = getGreenColorF();
		this.initialB = getBlueColorF();
	}

	@Override
	public void onUpdateGlow() {
		super.onUpdateGlow();
		float life = (float) particleAge / (float) particleMaxAge;
		particleGreen = initialG - 0.75F * life;
		particleBlue = initialB - 0.45F * life;
	}
}
