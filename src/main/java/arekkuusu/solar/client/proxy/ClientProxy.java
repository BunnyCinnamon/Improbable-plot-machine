/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.proxy;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.entanglement.quantum.data.IQuantumData;
import arekkuusu.solar.client.effect.SoundBase;
import arekkuusu.solar.client.render.ModRenders;
import arekkuusu.solar.client.util.RenderBakery;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.baker.DummyModelLoader;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.client.util.helper.ParticleRenderer;
import arekkuusu.solar.client.util.resource.SpriteLoader;
import arekkuusu.solar.common.proxy.IProxy;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class was created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements IProxy {

	public static final ParticleRenderer PARTICLE_RENDERER = new ParticleRenderer();

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		ModelHandler.registerModels();
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		registry.register(new SoundBase("spark"));
	}

	@SubscribeEvent
	public static void onSpriteRegister(TextureStitchEvent event) {
		TextureMap map = event.getMap();
		ResourceLibrary.ATLAS_SET.forEach(map::registerSprite);
	}

	//----------------Particle Renderer Start----------------//
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onTick(TickEvent.ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			PARTICLE_RENDERER.update();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRenderAfterWorld(RenderWorldLastEvent event) {
		GlStateManager.pushMatrix();
		PARTICLE_RENDERER.renderAll(event.getPartialTicks());
		GlStateManager.popMatrix();
	}
	//----------------Particle Renderer End----------------//

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
				.registerReloadListener(SpriteLoader.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		ModelLoaderRegistry.registerLoader(new DummyModelLoader());
		ModRenders.preInit();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		SpriteLibrary.init();
		ShaderLibrary.init();
		RenderBakery.bake();
		ModRenders.init();
	}

	@SubscribeEvent
	public static void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		SolarApi.getRelativityPowerMap().clear();
		SolarApi.getRelativityMap().clear();
		SolarApi.setQuantumData(null);
	}
}
