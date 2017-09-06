/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.proxy;

import arekkuusu.solar.client.render.ModRenders;
import arekkuusu.solar.client.render.ParticleRenderer;
import arekkuusu.solar.client.util.ModelBakery;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.baker.DummyModelLoader;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.client.util.resource.SpriteLoader;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.proxy.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public static void onTextureAtlasSprite(TextureStitchEvent event) {
		TextureMap map = event.getMap();
		map.registerSprite(ResourceLibrary.NOTHING);
		map.registerSprite(ResourceLibrary.PRIMAL_STONE);
		for(ResourceLocation location : ResourceLibrary.PRIMAL_GLYPH) {
			map.registerSprite(location);
		}
		map.registerSprite(ResourceLibrary.GRAVITY_HOPPER);
		for(ResourceLocation location : ResourceLibrary.GRAVITY_HOPPER_GLYPH) {
			map.registerSprite(location);
		}
		map.registerSprite(ResourceLibrary.SCHRODINGER_GLYPH);
		map.registerSprite(ResourceLibrary.BLINKER_BASE);
		map.registerSprite(ResourceLibrary.BLINKER_TOP);
		map.registerSprite(ResourceLibrary.BLINKER_BOTTOM);
	}

	//----------------Particle Renderer Start----------------//
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onTick(TickEvent.ClientTickEvent event) {
		if(event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
			PARTICLE_RENDERER.update();
		}
	}

	@SubscribeEvent
	public static void onRenderAfterWorld(RenderWorldLastEvent event) {
		if(Solar.PROXY instanceof ClientProxy) {
			GlStateManager.pushMatrix();
			PARTICLE_RENDERER.renderAll(event.getPartialTicks());
			GlStateManager.popMatrix();
		}
	}
	//----------------Particle Renderer End----------------//

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager())
				.registerReloadListener(SpriteLoader.INSTANCE);
		ModelLoaderRegistry.registerLoader(new DummyModelLoader());
		OBJLoader.INSTANCE.addDomain(LibMod.MOD_ID);
		ModRenders.preInit();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		SpriteLibrary.init();
		ModelBakery.bake();
		ModRenders.init();
	}
}
