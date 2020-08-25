package arekkuusu.implom.common.item;

import arekkuusu.implom.IPM;
import arekkuusu.implom.LibNames;
import arekkuusu.implom.common.block.ModBlocks;
import arekkuusu.implom.common.handler.CreativeTabHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public final class ModItems {

    public static final RegistryObject<Item> FIRE_CLAY_BLOCK = IPM.ITEMS.register(
            LibNames.FIRE_CLAY_BLOCK, () -> new BlockItem(ModBlocks.FIRE_CLAY_BLOCK.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_BRICK_BLOCK = IPM.ITEMS.register(
            LibNames.FIRE_BRICK_BLOCK, () -> new BlockItem(ModBlocks.FIRE_BRICK_BLOCK.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_BRICKS = IPM.ITEMS.register(
            LibNames.FIRE_BRICKS, () -> new BlockItem(ModBlocks.FIRE_BRICKS.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_BRICKS_GLASS = IPM.ITEMS.register(
            LibNames.FIRE_BRICKS_GLASS, () -> new BlockItem(ModBlocks.FIRE_BRICKS_GLASS.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_BRICKS_WINDOW = IPM.ITEMS.register(
            LibNames.FIRE_BRICKS_WINDOW, () -> new BlockItem(ModBlocks.FIRE_BRICKS_WINDOW.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_CONTROLLER = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_CONTROLLER, () -> new BlockItem(ModBlocks.BLAST_FURNACE_CONTROLLER.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_DRAIN = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_DRAIN, () -> new BlockItem(ModBlocks.BLAST_FURNACE_DRAIN.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_FILTER = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_FILTER, () -> new BlockItem(ModBlocks.BLAST_FURNACE_FILTER.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_INPUT = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_INPUT, () -> new BlockItem(ModBlocks.BLAST_FURNACE_INPUT.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_TUYERE = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_TUYERE, () -> new BlockItem(ModBlocks.BLAST_FURNACE_TUYERE.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_AIR_VENT = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_AIR_VENT, () -> new BlockItem(ModBlocks.BLAST_FURNACE_AIR_VENT.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_AIR_PUMP = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_AIR_PUMP, () -> new BlockItem(ModBlocks.BLAST_FURNACE_AIR_PUMP.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_PIPE = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_PIPE, () -> new BlockItem(ModBlocks.BLAST_FURNACE_PIPE.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> HOT_BLAST_HEATER = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_HEATER, () -> new BlockItem(ModBlocks.BLAST_FURNACE_HEATER.get(), defaultItemProperties())
    );
    public static final RegistryObject<Item> BLAST_FURNACE_PIPE_GAUGE = IPM.ITEMS.register(
            LibNames.BLAST_FURNACE_PIPE_GAUGE, () -> new BlockItem(ModBlocks.BLAST_FURNACE_PIPE_GAUGE.get(), defaultItemProperties())
    );

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties().group(CreativeTabHandler.MISC);
    }

    public static void init() {
    }
}
