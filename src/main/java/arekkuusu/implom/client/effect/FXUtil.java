/*
 * Arekkuusu / Improbable Plot Machine 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import arekkuusu.implom.client.util.SpriteLibrary;
import net.katsstuff.teamnightclipse.mirror.Mirror;
import net.katsstuff.teamnightclipse.mirror.client.ClientProxy;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.client.particles.IMirrorParticle;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by <Arekkuusu> on 27/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public final class FXUtil {

	public static void playSound(World world, BlockPos pos, SoundEvent event, SoundCategory category, float volume) {
		if(world instanceof WorldClient) {
			((WorldClient) world).playSound(pos, event, category, volume, 1F, false);
		}
	}

	public static void spawnMute(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light type) {
		add(new ParticleMute(world, pos, speed, scale, age, rgb, type));
	}

	public static void spawnSpeck(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		add(new ParticleSpeck(world, pos, speed, scale, age, rgb, glow));
	}

	public static void spawnNeutronBlast(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, boolean collide) {
		add(new ParticleNeutronBlast(world, pos, speed, scale, age, rgb, collide));
	}

	public static void spawnLuminescence(World world, Vector3 pos, Vector3 speed, int age, float scale, GlowTexture glow) {
		add(new ParticleLuminescence(world, pos, speed, scale, age, glow));
	}

	public static void spawnDepthTunneling(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		add(new ParticleDepthTunneling(world, pos, speed, age, scale, rgb, glow));
	}

	public static void spawnArcDischarge(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch, boolean fade) {
		add(new ParticleArcDischarge(world, from, to, generations, offset, age, rgb, branch, fade));
	}

	public static void spawnSquared(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {
		ParticleBase particle = new ParticleBase(world, pos, speed, scale, age, rgb);
		particle.setSprite(SpriteLibrary.SQUARED_PARTICLE);
		add(particle);
	}

	public static void spawnBeam(World world, Vector3 from, Vector3 direction, float distance, int amount, float size, int color) {
		for(float i = 0; i + 1 < distance * amount; ++i) {
			float offset = (i / amount);
			Vector3 posVec = from.add(direction.x() * offset, direction.y() * offset, direction.z() * offset);
			spawnSpeck(world, posVec, Vector3.Zero(), 60, size * MathHelper.cos((float) ((2 * Math.PI) * (i / (distance * amount)))), color, GlowTexture.MOTE);
		}
	}

	public static void add(IMirrorParticle particle) {
		((ClientProxy) Mirror.proxy()).particleRenderer().addParticle(particle);
	}

	@SideOnly(Side.CLIENT)
	private static boolean doParticle() {
		int setting = Minecraft.getMinecraft().gameSettings.particleSetting;
		float chance;
		switch (setting) {
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
