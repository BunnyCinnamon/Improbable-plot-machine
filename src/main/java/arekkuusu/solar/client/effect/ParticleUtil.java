/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

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

	public static void spawnQuorn(World world, double xCoord, double yCoord, double zCoord, double speed, double xPoint, double yPoint, double zPoint, float scale, int rgb) {
		if(doParticle()) {
			ParticleQuorn particle = new ParticleQuorn(world, xCoord, yCoord, zCoord, speed, xPoint, yPoint, zPoint, scale, rgb);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnNeutron(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleNeutron particle = new ParticleNeutron(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, age, scale, rgb);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnNeutronBlast(World world, double xCoord, double yCoord, double zCoord, double speed, double xPoint, double yPoint, double zPoint, int rgb, float scale, boolean collide) {
		if(doParticle()) {
			ParticleNeutronBlast particle = new ParticleNeutronBlast(world, xCoord, yCoord, zCoord, speed, xPoint, yPoint, zPoint, rgb, scale, collide);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnLightParticle(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleLight particle = new ParticleLight(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, rgb, age, scale);
			ClientProxy.PARTICLE_RENDERER.add(particle);
		}
	}

	public static void spawnTunnelingPhoton(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int rgb, int age, float scale) {
		if(doParticle()) {
			ParticleTunnelingPhoton particle = new ParticleTunnelingPhoton(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, age, scale, rgb);
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
