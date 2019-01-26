/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.common.api.ApiInstance;
import arekkuusu.implom.common.handler.data.capability.ModCapability;
import arekkuusu.implom.common.handler.data.WorldNBTData;
import arekkuusu.implom.common.handler.gen.ModGen;
import arekkuusu.implom.common.lib.LibMod;
import arekkuusu.implom.common.network.GuiHandler;
import arekkuusu.implom.common.network.PacketHandler;
import arekkuusu.implom.common.proxy.IProxy;
import arekkuusu.implom.common.theorem.ModTheorems;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.InstanceFactory;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@Mod( // *evil chuckles*
		modid = LibMod.MOD_ID,
		name = LibMod.MOD_NAME,
		version = LibMod.MOD_VERSION,
		dependencies = LibMod.MOD_DEPENDENCY,
		acceptedMinecraftVersions = "[1.12.2]"
)
public final class IPM {

	@SidedProxy(clientSide = LibMod.CLIENT_PROXY, serverSide = LibMod.SERVER_PROXY)
	private static IProxy proxy;
	private static final IPM INSTANCE = new IPM();
	public static final Logger LOG = LogManager.getLogger(LibMod.MOD_NAME);

	static {
		FluidRegistry.enableUniversalBucket();
	}

	private IPM() {
	}

	public static IProxy getProxy() {
		return proxy;
	}

	@InstanceFactory
	public static IPM getInstance() {
		return INSTANCE;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		IPMApi.setInstance(new ApiInstance());
		WorldNBTData.init(event.getAsmData());
		PacketHandler.init();
		ModCapability.init();
		ModTheorems.init();
		ModGen.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	@EventHandler
	public void close(FMLServerStoppedEvent event) {
		IPMApi.getInstance().unloadWorld();
	}
}
