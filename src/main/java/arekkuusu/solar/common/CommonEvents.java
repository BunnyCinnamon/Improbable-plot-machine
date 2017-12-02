/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.handler.recipe.ModRecipes;
import arekkuusu.solar.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by <Arekkuusu> on 23/06/2017.
 * It's distributed as part of Solar.
 */
@Mod.EventBusSubscriber
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
	public static void syncQuantumData(PlayerEvent.PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			WorldQuantumData.syncTo((EntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public static void unsyncQuantumData(PlayerEvent.PlayerLoggedOutEvent event) {
		if(event.player instanceof EntityPlayerSP) {
			SolarApi.getEntangledStacks().clear();
		}
	}

	@SubscribeEvent
	public static void loadQuantumData(WorldEvent.Load event) {
		Solar.LOG.info("[WorldQuantumData] Loading Quantum Data");
		WorldQuantumData.get(event.getWorld());
	}
}
