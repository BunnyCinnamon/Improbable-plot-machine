/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import net.katsstuff.mirror.data.Vector3;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 23/07/2017.
 * It's distributed as part of Solar.
 */
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
