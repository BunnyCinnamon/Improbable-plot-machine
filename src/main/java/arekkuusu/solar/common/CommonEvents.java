/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.client.effect.SoundBase;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.handler.recipe.ModRecipes;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/*
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
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
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		IForgeRegistry<SoundEvent> registry = event.getRegistry();
		registry.register(new SoundBase("spark"));
	}

	@SubscribeEvent
	public static void loadQuantumData(WorldEvent.Load event) {
		if(SolarApi.getWorldData() == null) {
			Solar.LOG.info("[WorldQuantumData] Loading Data");
			SolarApi.setWorldData(WorldQuantumData.get(event.getWorld()));
		}
	}
}
