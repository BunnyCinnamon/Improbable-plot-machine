/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.effect;

import arekkuusu.solar.client.util.SpriteLibrary;
import net.katsstuff.mirror.Mirror;
import net.katsstuff.mirror.client.ClientProxy;
import net.katsstuff.mirror.client.particles.GlowTexture;
import net.katsstuff.mirror.client.particles.IMirrorParticle;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 27/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings({"MethodCallSideOnly", "LocalVariableDeclarationSideOnly", "VariableUseSideOnly", "NewExpressionSideOnly"})
public final class FXUtil { //A cLaSs ANnOtAtED wItH @SIdEOnLy cAN oNLy Be uSEd iN oThER mAtChIng aNnotAteD cLaSses aNd mEtHodS

	public static void playSound(World world, BlockPos pos, SoundEvent event, SoundCategory category, float volume) {
		if(world instanceof WorldClient) {
			((WorldClient) world).playSound(pos, event, category, volume, 1F, false);
		}
	}

	public static void spawnLight(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light type) {
		if(doParticle()) {
			add(new ParticleLight(world, pos, speed, scale, age, rgb, type));
		}
	}

	public static void spawnMute(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		if (doParticle()) {
			add(new ParticleMute(world, pos, speed, scale, age, rgb, glow));
		}
	}

	public static void spawnNeutron(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, boolean collide) {
		if(doParticle()) {
			add(new ParticleNeutron(world, pos, speed, scale, age, rgb, collide));
		}
	}

	public static void spawnLumen(World world, Vector3 pos, Vector3 speed, int age, float scale, GlowTexture glow) {
		if(doParticle()) {
			add(new ParticleLuminescence(world, pos, speed, scale, age, glow));
		}
	}

	public static void spawnTunneling(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		if(doParticle()) {
			add(new ParticleTunneling(world, pos, speed, age, scale, rgb, glow));
		}
	}

	public static void spawnVolt(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch, boolean fade) {
		if(doParticle()) {
			add(new ParticleVolt(world, from, to, generations, offset, age, rgb, branch, fade));
		}
	}

	public static void spawnSquared(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {
		if(doParticle()) {
			ParticleBase particle = new ParticleBase(world, pos, speed, scale, age, rgb);
			particle.setSprite(SpriteLibrary.SQUARED_PARTICLE);
			add(particle);
		}
	}

	public static void addBeam(World world, Vector3 from, Vector3 direction, float distance, int amount, float size, int color) {
		for(float i = 0; i + 1 < distance * amount; ++i) {
			float offset = (i / amount);
			Vector3 posVec = from.add(direction.x() * offset, direction.y() * offset, direction.z() * offset);
			spawnMute(world, posVec, Vector3.Zero(), 60, size * MathHelper.cos((float) ((2 * Math.PI) * (i / (distance * amount)))), color, GlowTexture.MOTE);
		}
	}

	public static void add(IMirrorParticle particle) {
		((ClientProxy) Mirror.proxy()).particleRenderer().addParticle(particle);
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
