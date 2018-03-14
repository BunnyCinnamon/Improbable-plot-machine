/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.proxy;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.network.PacketHelper;
import net.minecraft.entity.player.EntityPlayerMP;
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
 * It's distributed as part of Solar.
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
			Solar.LOG.info("[WorldQuantumData] Binding Data to player:", event.player.getName());
			PacketHelper.syncQuantumTo((EntityPlayerMP) event.player);
		}
	}

	@Mod.EventHandler
	public static void serverStop(FMLServerStoppedEvent event) {
		SolarApi.getRelativityMap().clear();
		SolarApi.getSimpleLinkMap().clear();
		SolarApi.getDataMap().clear();
		SolarApi.setQuantumData(null);
	}
}
