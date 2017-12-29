/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 27/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings({"MethodCallSideOnly", "LocalVariableDeclarationSideOnly", "VariableUseSideOnly", "NewExpressionSideOnly"})
public class ParticleUtil { //A cLaSs ANnOtAtED wItH @SIdEOnLy cAN oNLy Be uSEd iN oThER mAtChIng aNnotAteD cLaSses aNd mEtHodS

	public static void spawnQuorn(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {
		if(doParticle()) {
			ParticleQuorn particle = new ParticleQuorn(world, pos, speed, age, scale, rgb);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnNeutron(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleNeutron particle = new ParticleNeutron(world, pos, speed, age, scale, rgb);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnNeutronBlast(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale, boolean collide) {
		if(doParticle()) {
			ParticleNeutronBlast particle = new ParticleNeutronBlast(world, pos, speed, age, rgb, scale, collide);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnLightParticle(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleLight particle = new ParticleLight(world, pos, speed, rgb, age, scale);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnTunnelingPhoton(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleTunnelingPhoton particle = new ParticleTunnelingPhoton(world, pos, speed, age, scale, rgb);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnChargedIce(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleDryIce dryIce = new ParticleDryIce(world, pos, speed, rgb, age, scale);
			ClientProxy.PARTICLE_RENDERER.add(dryIce);
		}
	}

	public static void spawnBolt(World world, Vector3 from, Vector3 to, int generations, float offset, int rgb, boolean branch, boolean fade) {
		if(doParticle()) {
			ParticleBolt bolt = new ParticleBolt(world, from, to, generations, offset, rgb, branch, fade);
			ClientProxy.PARTICLE_RENDERER.add(bolt);
		}
	}

	public static void spawnDarkParticle(World world, Vector3 pos, Vector3 speed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleDark particle = new ParticleDark(world, pos, speed, rgb, age, scale);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	@SideOnly(Side.CLIENT)
	private static boolean doParticle() {
		if(FMLCommonHandler.instance().getEffectiveSide().isServer()) return false;
		int setting = Minecraft.getMinecraft().gameSettings.particleSetting;

		float chance;
		switch(setting) {
			case 1:
				chance = 0.6F;
				break;
			case 2:
				chance = 0.2F;
				break;
			default:
				return true;
		}

		return Math.random() < chance;
	}
}
