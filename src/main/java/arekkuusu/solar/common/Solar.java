/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common;

import arekkuusu.solar.common.entity.ModEntities;
import arekkuusu.solar.common.handler.data.ModCapability;
import arekkuusu.solar.common.handler.data.WorldQuantumData;
import arekkuusu.solar.common.handler.gen.ModGen;
import arekkuusu.solar.common.lib.LibMod;
import arekkuusu.solar.common.network.GuiHandler;
import arekkuusu.solar.common.network.PacketHandler;
import arekkuusu.solar.common.proxy.IProxy;
import arekkuusu.solar.common.theorem.ModTheorems;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.fml.common.Mod.EventHandler;
import static net.minecraftforge.fml.common.Mod.Instance;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
@Mod( // *evil chuckles*
	modid = LibMod.MOD_ID,
	name = LibMod.MOD_NAME,
	version = LibMod.MOD_VERSION,
	dependencies = "required-after:mirror;",
	acceptedMinecraftVersions = "[1.12.2]"
)
public class Solar {

	@SidedProxy(clientSide = LibMod.CLIENT_PROXY, serverSide = LibMod.SERVER_PROXY)
	public static IProxy PROXY;
	@Instance
	public static Solar INSTANCE;
	public static Logger LOG = LogManager.getLogger(LibMod.MOD_NAME);

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PROXY.preInit(event);
		WorldQuantumData.init(event.getAsmData());
		PacketHandler.init();
		ModCapability.init();
		ModEntities.init();
		ModTheorems.init();
		ModGen.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}
}
