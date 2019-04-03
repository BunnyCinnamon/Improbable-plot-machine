/*
 * Arekkuusu / Improbable Plot Machine 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.effect;

import net.katsstuff.teamnightclipse.mirror.Mirror;
import net.katsstuff.teamnightclipse.mirror.client.ClientProxy;
import net.katsstuff.teamnightclipse.mirror.client.particles.IMirrorParticle;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
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

	public static void spawnSpeck(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light light, ResourceLocation location) {
		add(new ParticleBase(world, pos, speed, scale, age, rgb, light, location));
	}

	public static void spawnLuminescence(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light light, ResourceLocation location) {
		add(new ParticleLuminescence(world, pos, speed, scale, age, rgb, light, location));
	}

	public static void spawnNeutronBlast(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light light, ResourceLocation location, boolean collide) {
		add(new ParticleNeutronBlast(world, pos, speed, scale, age, rgb, light, location, collide));
	}

	public static void spawnArcDischarge(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch, boolean fade) {
		add(new ParticleArcDischarge(world, from, to, generations, offset, age, rgb, branch, fade));
	}

	public static void spawnBeam(World world, Vector3 from, Vector3 direction, float distance, int amount, float size, int rgb, Light light, ResourceLocation location) {
		for(float i = 0; i + 1 < distance * amount; ++i) {
			float offset = (i / amount);
			Vector3 posVec = from.add(direction.x() * offset, direction.y() * offset, direction.z() * offset);
			spawnSpeck(world, posVec, Vector3.Zero(), 60, size * MathHelper.cos((float) ((2 * Math.PI) * (i / (distance * amount)))), rgb, light, location);
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
