package cinnamon.implom;

import cinnamon.implom.client.ClientProxy;
import cinnamon.implom.client.render.tile.TileBlastFurnaceControllerRenderer;
import cinnamon.implom.client.render.tile.TileBlastFurnaceInputRenderer;
import cinnamon.implom.common.ServerProxy;
import cinnamon.implom.common.block.ModBlocks;
import cinnamon.implom.common.block.fluid.ModFluids;
import cinnamon.implom.common.block.tile.ModTiles;
import cinnamon.implom.common.handler.recipe.ModRecipes;
import cinnamon.implom.common.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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

    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, IPM.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, IPM.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, IPM.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IPM.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IPM.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, IPM.MOD_ID);

    public static final String MOD_ID = "improbable_plot_machine";
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
        IPM.FLUID_TYPES.register(modBus);
        IPM.FLUIDS.register(modBus);
        IPM.BLOCKS.register(modBus);
        IPM.TILES.register(modBus);
        IPM.ITEMS.register(modBus);
        IPM.CONTAINERS.register(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::buildContents);
        modBus.addListener(this::onModConfigEvent);
        //Forge Bus
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
    }

    public void commonSetup(FMLCommonSetupEvent event) {

    }

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FIRE_BRICKS_GLASS.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FIRE_BRICKS_WINDOW.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLAST_FURNACE_CONTROLLER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLAST_FURNACE_AIR_VENT.get(), RenderType.translucent());
        BlockEntityRenderers.register(ModTiles.BLAST_FURNACE_CONTROLLER.get(), TileBlastFurnaceControllerRenderer::new);
        BlockEntityRenderers.register(ModTiles.BLAST_FURNACE_INPUT.get(), TileBlastFurnaceInputRenderer::new);
    }

    public void buildContents(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "example"), builder ->
                // Set name of tab to display
                builder.title(Component.literal(IPM.MOD_ID + "." + "misc_tab"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(ModItems.FIRE_BRICK_BLOCK.get()))
                        // Add default items to tab
                        .displayItems((params, output) -> {
                            IPM.ITEMS.getEntries().forEach(itemRegistryObject -> {
                                output.accept(itemRegistryObject.get());
                            });
                        })
        );
    }

    public void onModConfigEvent(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == IPMConfig.Holder.CLIENT_SPEC) {
            IPMConfig.Setup.client(config);
            LOG.debug("Baked client config");
        } else if (config.getSpec() == IPMConfig.Holder.COMMON_SPEC) {
            IPMConfig.Setup.server(config);
            ModRecipes.init();
            LOG.debug("Baked server config");
        }
    }
}
