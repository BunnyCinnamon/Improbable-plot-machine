/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.proxy;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.client.render.ModRenders;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.baker.DummyModelLoader;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.client.util.resource.SpriteManager;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class was created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
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
		SolarApi.getRelativityPowerMap().clear();
		SolarApi.getRelativityMap().clear();
		SolarApi.setQuantumData(null);
	}

	public static void registerResourceReloadListener(IResourceManagerReloadListener listener) {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
				.registerReloadListener(listener);
	}
}
