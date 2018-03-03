package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.util.Vector3;
import net.katsstuff.mirror.client.particles.GlowTexture;
import net.minecraft.world.World;

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
