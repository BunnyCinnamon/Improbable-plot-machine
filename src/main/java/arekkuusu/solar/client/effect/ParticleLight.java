package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.util.Vector3;
import net.minecraft.world.World;

public class ParticleLight extends ParticleBase {

	private Light type;

	ParticleLight(World world, Vector3 pos, Vector3 speed, float scale, int age, int rgb, Light type) {
		super(world, pos, speed, scale, age, rgb);
		setParticleTexture(type.location);
		this.type = type;
	}

	@Override
	public boolean isAdditive() {
		return type.additive;
	}

	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		return type.additive ? 255 : 0;
	}
}
