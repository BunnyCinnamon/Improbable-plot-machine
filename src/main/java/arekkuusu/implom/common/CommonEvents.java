/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.client.effect.SoundBase;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.entity.ModEntities;
import arekkuusu.implom.common.handler.recipe.ModRecipes;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.registries.IForgeRegistry;

/*
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@EventBusSubscriber(modid = LibMod.MOD_ID)
public final class CommonEvents {

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModItems.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ModRecipes.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		ModEntities.register(event.getRegistry());
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		registry.register(new SoundBase("spark"));
	}

	@SubscribeEvent
	public static void loadWorldData(WorldEvent.Load event) {
		IPMApi.getInstance().loadWorld(event.getWorld());
	}

	@SubscribeEvent
	public static void unloadWorldData(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		IPMApi.getInstance().unloadWorld();
	}
}
