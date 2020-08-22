package arekkuusu.implom;

import arekkuusu.implom.client.ClientProxy;
import arekkuusu.implom.common.ServerProxy;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.block.fluid.ModFluids;
import arekkuusu.implom.common.block.tile.ModTiles;
import arekkuusu.implom.common.handler.recipe.ModRecipes;
import arekkuusu.implom.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as Improbable plot machine.
 */
@Mod(IPM.MOD_ID)
public final class IPM {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, IPM.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, IPM.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IPM.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IPM.MOD_ID);

    public static final String MOD_ID = "improbableplotmachine";
    public static final Logger LOG = LogManager.getLogger(IPM.MOD_ID);
    public static Proxy proxy = new Proxy() {
    };

    public static Proxy getProxy() {
        return proxy;
    }

    public IPM() {
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, IPMConfig.Holder.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, IPMConfig.Holder.COMMON_SPEC);
        //Mod Bus
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModFluids.init();
        ModBlocks.init();
        ModItems.init();
        ModTiles.init();
        ModRecipes.init();
        IPM.FLUIDS.register(modBus);
        IPM.BLOCKS.register(modBus);
        IPM.TILES.register(modBus);
        IPM.ITEMS.register(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::onModConfigEvent);
        modBus.addListener(this::onFingerprintViolation);
        //Forge Bus
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::serverStarting);
        forgeBus.addListener(this::serverStopping);
    }

    public void commonSetup(FMLCommonSetupEvent event) {

    }

    public void clientSetup(FMLClientSetupEvent event) {

    }

    public void onModConfigEvent(ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == IPMConfig.Holder.CLIENT_SPEC) {
            IPMConfig.Setup.client(config);
            LOG.debug("Baked client config");
        } else if (config.getSpec() == IPMConfig.Holder.COMMON_SPEC) {
            IPMConfig.Setup.server(config);
            LOG.debug("Baked server config");
        }
    }

    public void onFingerprintViolation(final FMLFingerprintViolationEvent event) {
        LOG.warn("Invalid fingerprint detected!");
    }

    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }
}
