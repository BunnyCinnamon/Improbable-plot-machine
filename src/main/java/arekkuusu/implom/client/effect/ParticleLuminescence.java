/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/*
 * Created by <Arekkuusu> on 4/27/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class ParticleLuminescence extends ParticleBase {

	private final float initialG;
	private final float initialB;

	ParticleLuminescence(World world, Vector3 pos, Vector3 speed, float scale, int age, int rgb, Light light, ResourceLocation location) {
		super(world, pos, speed, scale, age, rgb, light, location);
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
