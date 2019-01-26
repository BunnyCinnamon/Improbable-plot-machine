/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.proxy;

import arekkuusu.implom.client.effect.Light;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
public interface IProxy {

	void preInit(FMLPreInitializationEvent event);

	void init(FMLInitializationEvent event);

	default void playSound(World world, BlockPos pos, SoundEvent event, SoundCategory category, float volume) {
		//NO-OP
	}

	default void spawnMute(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light type) {
		//NO-OP
	}

	default void spawnSpeck(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		//NO-OP
	}

	default void spawnNeutronBlast(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, boolean collide) {
		//NO-OP
	}

	default void spawnLuminescence(World world, Vector3 pos, Vector3 speed, int age, float scale, GlowTexture glow) {
		//NO-OP
	}

	default void spawnDepthTunneling(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		//NO-OP
	}

	default void spawnArcDischarge(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch, boolean fade) {
		//NO-OP
	}

	default void spawnSquared(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {
		//NO-OP
	}

	default void spawnBeam(World world, Vector3 from, Vector3 direction, float distance, int amount, float size, int color) {
		//NO-OP
	}
}
