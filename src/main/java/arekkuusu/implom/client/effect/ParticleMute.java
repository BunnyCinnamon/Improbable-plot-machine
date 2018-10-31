/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.world.World;

/*
 * Created by <Arekkuusu> on 23/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class ParticleMute extends ParticleBase {

	private Light type;

	ParticleMute(World world, Vector3 pos, Vector3 speed, float scale, int age, int rgb, Light type) {
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
