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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 26/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class ParticleNeutron extends ParticleBase {

	private final float initScale;
	private final boolean dark;

	ParticleNeutron(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {
		super(world, pos, speed, rgb);
		particleMaxAge = age;
		particleScale = scale;
		initScale = particleScale;
		canCollide = false;
		dark = rgb == 0x000000;
		setSprite(dark ? SpriteLibrary.DARK_PARTICLE : SpriteLibrary.NEUTRON_PARTICLE);
	}

	@Override
	public void onUpdate() {
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}
		float life = (float) particleAge / (float) particleMaxAge;
		this.particleScale = initScale - initScale * life;
		this.particleAlpha = 0.25F * (1F - life);

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.move(this.motionX, this.motionY, this.motionZ);
	}

	@Override
	public int getBrightnessForRender(float idk) {
		return dark ? 0 : 255;
	}

	@Override
	public boolean isAdditive() {
		return !dark;
	}
}
