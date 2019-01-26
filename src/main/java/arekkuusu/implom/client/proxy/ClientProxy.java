/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.proxy;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.client.ModRenders;
import arekkuusu.implom.client.effect.FXUtil;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.SpriteLibrary;
import arekkuusu.implom.client.util.baker.DummyModelLoader;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.client.util.resource.SpriteManager;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.proxy.IProxy;
import net.katsstuff.teamnightclipse.mirror.client.particles.GlowTexture;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = LibMod.MOD_ID, value = Side.CLIENT)
public class ClientProxy implements IProxy {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelHandler.registerModels();
	}

	@SubscribeEvent
	public static void registerSprites(TextureStitchEvent event) {
		TextureMap map = event.getMap();
		ResourceLibrary.ATLAS_SET.forEach(map::registerSprite);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		registerResourceReloadListener(SpriteManager.INSTANCE);
		registerResourceReloadListener(DummyModelLoader.INSTANCE);
		ModelLoaderRegistry.registerLoader(DummyModelLoader.INSTANCE);
		ModRenders.preInit();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		SpriteLibrary.init();
		ShaderLibrary.init();
		RenderHelper.bake();
		ModRenders.init();
	}

	@SubscribeEvent
	public static void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		IPMApi.getInstance().unloadWorld();
	}

	private static void registerResourceReloadListener(ISelectiveResourceReloadListener listener) {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(listener);
	}

	@Override
	public void playSound(World world, BlockPos pos, SoundEvent event, SoundCategory category, float volume) {
		FXUtil.playSound(world, pos, event, category, volume);
	}

	@Override
	public void spawnMute(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light type) {
		if(canParticleSpawn()) FXUtil.spawnMute(world, pos, speed, age, scale, rgb, type);
	}

	@Override
	public void spawnSpeck(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		if(canParticleSpawn()) FXUtil.spawnSpeck(world, pos, speed, age, scale, rgb, glow);
	}

	@Override
	public void spawnNeutronBlast(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, boolean collide) {
		if(canParticleSpawn()) FXUtil.spawnNeutronBlast(world, pos, speed, age, scale, rgb, collide);
	}

	@Override
	public void spawnLuminescence(World world, Vector3 pos, Vector3 speed, int age, float scale, GlowTexture glow) {
		if(canParticleSpawn()) FXUtil.spawnLuminescence(world, pos, speed, age, scale, glow);
	}

	@Override
	public void spawnDepthTunneling(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, GlowTexture glow) {
		if(canParticleSpawn()) FXUtil.spawnDepthTunneling(world, pos, speed, age, scale, rgb, glow);
	}

	@Override
	public void spawnArcDischarge(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch, boolean fade) {
		if(canParticleSpawn()) FXUtil.spawnArcDischarge(world, from, to, generations, offset, age, rgb, branch, fade);
	}

	@Override
	public void spawnSquared(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb) {
		if(canParticleSpawn()) FXUtil.spawnSquared(world, pos, speed, age, scale, rgb);
	}

	@Override
	public void spawnBeam(World world, Vector3 from, Vector3 direction, float distance, int amount, float size, int color) {
		if(canParticleSpawn()) FXUtil.spawnBeam(world, from, direction, distance, amount, size, color);
	}

	private boolean canParticleSpawn() {
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
