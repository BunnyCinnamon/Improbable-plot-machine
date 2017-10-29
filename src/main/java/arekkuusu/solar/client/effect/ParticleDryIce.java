/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.client.util.SpriteLibrary;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 26/10/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class ParticleDryIce extends ParticleBase {

	private final float initScale;

	ParticleDryIce(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int rgb, int age, float scale) {
		super(world, xCoord, yCoord, zCoord, 0, 0, 0);
		float r = (rgb >>> 16 & 0xFF) / 256.0F;
		float g = (rgb >>> 8 & 0xFF) / 256.0F;
		float b = (rgb & 0xFF) / 256.0F;
		setRBGColorF(r, g, b);
		particleMaxAge = age;
		particleScale = scale;
		initScale = particleScale;
		particleGravity = initScale * 0.1F;
		particleAngle = rand.nextBoolean() ? 2F : -2F * (float) Math.PI;
		canCollide = false;

		motionX = xSpeed;
		motionY = ySpeed;
		motionZ = zSpeed;

		setSprite(SpriteLibrary.CHARGED_ICE);
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
}
