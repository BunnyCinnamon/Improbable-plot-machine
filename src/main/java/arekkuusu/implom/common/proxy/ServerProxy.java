/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.proxy;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.network.PacketHelper;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.SERVER)
@EventBusSubscriber(modid = LibMod.MOD_ID, value = Side.SERVER)
public class ServerProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@SubscribeEvent
	public static void playerServerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			IPM.LOG.info("[WorldQuantumData] Binding Data to player:", event.player.getName());
			PacketHelper.syncQuantumTo((EntityPlayerMP) event.player);
		}
	}

	@Mod.EventHandler
	public static void serverStop(FMLServerStoppedEvent event) {
		IPMApi.getRelativityMap().clear();
		IPMApi.getBinaryMap().clear();
		IPMApi.setWorldData(null);
	}

	@Override
	public void playSound(World world, BlockPos pos, SoundEvent event, SoundCategory category, float volume) {

	}

	@Override
	public void spawnMute(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light type) {

	}

	@Override
	public void spawnSpeck(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {

	}

	@Override
	public void spawnNeutronBlast(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, boolean collide) {

	}

	@Override
	public void spawnLuminescence(World world, Vector3 pos, Vector3 speed, int age, float scale, GlowTexture glow) {

	}

	@Override
	public void spawnDepthTunneling(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {

	}

	@Override
	public void spawnArcDischarge(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch, boolean fade) {

	}

	@Override
	public void spawnSquared(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {

	}

	@Override
	public void spawnBeam(World world, Vector3 from, Vector3 direction, float distance, int amount, float size, int color) {

	}
}
