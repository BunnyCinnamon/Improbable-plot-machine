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
import arekkuusu.implom.client.util.BakerLibrary;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.SpriteLibrary;
import arekkuusu.implom.client.util.baker.DummyModelLoader;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.proxy.IProxy;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
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
		ModelHelper.registerModels();
	}

	@SubscribeEvent
	public static void stitchTextures(TextureStitchEvent event) {
		BakerLibrary.stitchTextureModels();
		TextureMap map = event.getMap();
		ResourceLibrary.ATLAS_SET.forEach(map::registerSprite);
		map.registerSprite(ResourceLibrary.GLOW_PARTICLE);
		map.registerSprite(ResourceLibrary.SQUARE_PARTICLE);
		map.registerSprite(ResourceLibrary.DULL_PARTICLE);
		map.registerSprite(ResourceLibrary.VOLT_PARTICLE);
	}

	@SubscribeEvent
	public static void bakeModels(ModelBakeEvent event) {
		BakerLibrary.bakeModels();
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		registerResourceReloadListener(DummyModelLoader.INSTANCE);
		ModelLoaderRegistry.registerLoader(DummyModelLoader.INSTANCE);
		SpriteLibrary.preInit();
		ModRenders.preInit();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		ShaderLibrary.init();
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
		((WorldClient) world).playSound(pos, event, category, volume, 1F, false);
	}

	@Override
	public void spawnSpeck(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light light, ResourceLocation location) {
		if(canParticleSpawn()) FXUtil.spawnSpeck(world, pos, speed, age, scale, rgb, light, location);
	}

	@Override
	public void spawnNeutronBlast(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light light, ResourceLocation location, boolean collide) {
		if(canParticleSpawn()) FXUtil.spawnNeutronBlast(world, pos, speed, age, scale, rgb, light, location, collide);
	}

	@Override
	public void spawnLuminescence(World world, Vector3 pos, Vector3 speed, int age, float scale, int rgb, Light light, ResourceLocation location) {
		if(canParticleSpawn()) FXUtil.spawnLuminescence(world, pos, speed, age, scale, rgb, light, location);
	}

	@Override
	public void spawnArcDischarge(World world, Vector3 from, Vector3 to, int generations, float offset, int age, int rgb, boolean branch) {
		if(canParticleSpawn()) FXUtil.spawnArcDischarge(world, from, to, generations, offset, age, rgb, branch);
	}

	@Override
	public void spawnBeam(World world, Vector3 pos, Vector3 direction, float distance, int amount, float size, int rgb, Light light, ResourceLocation location) {
		if(canParticleSpawn()) FXUtil.spawnBeam(world, pos, direction, distance, amount, size, rgb, light, location);
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
