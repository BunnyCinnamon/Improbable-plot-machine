package cinnamon.implom.common.item;

import cinnamon.implom.IPM;
import cinnamon.implom.LibNames;
import cinnamon.implom.common.block.ModBlocks;
import cinnamon.implom.common.handler.CreativeTabHandler;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {

    public static final RegistryObject<Item> MAGNETIC_BOLT = IPM.ITEMS.register(
            LibNames.MAGNETIC_BOLT, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> MAGNETIC_GEAR = IPM.ITEMS.register(
            LibNames.MAGNETIC_GEAR, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> MAGNETIC_SPRING = IPM.ITEMS.register(
            LibNames.MAGNETIC_SPRING, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> CLOCKWORK = IPM.ITEMS.register(
            LibNames.CLOCKWORK, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> MAINSPRING_MECHANISM = IPM.ITEMS.register(
            LibNames.MAINSPRING_MECHANISM, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> MOMENTUM_OSCILLATOR = IPM.ITEMS.register(
            LibNames.MOMENTUM_OSCILLATOR, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> FRAME_CORE = IPM.ITEMS.register(
            LibNames.FRAME_CORE, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> SOLENOID = IPM.ITEMS.register(
            LibNames.SOLENOID, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> RESISTOR = IPM.ITEMS.register(
            LibNames.RESISTOR, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> CAPACITOR = IPM.ITEMS.register(
            LibNames.CAPACITOR, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> INDUCTOR = IPM.ITEMS.register(
            LibNames.INDUCTOR, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> GOLD_PLATE = IPM.ITEMS.register(
            LibNames.GOLD_PLATE, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> MAGNETIC_PLATE = IPM.ITEMS.register(
            LibNames.MAGNETIC_PLATE, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> MAGNETIC_ACTUATOR = IPM.ITEMS.register(
            LibNames.MAGNETIC_ACTUATOR, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> ELECTROMAGNET = IPM.ITEMS.register(
            LibNames.ELECTROMAGNET, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> INTRINSIC_CELL = IPM.ITEMS.register(
            LibNames.INTRINSIC_CELL, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> INTRINSIC_CAPACITOR = IPM.ITEMS.register(
            LibNames.INTRINSIC_CAPACITOR, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_BRICK = IPM.ITEMS.register(
            LibNames.FIRE_BRICK, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_BRICK_PLATE = IPM.ITEMS.register(
            LibNames.FIRE_BRICK_PLATE, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_CLAY = IPM.ITEMS.register(
            LibNames.FIRE_CLAY, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_CLAY_BRICK = IPM.ITEMS.register(
            LibNames.FIRE_CLAY_BRICK, () -> new Item(defaultItemProperties())
    );
    public static final RegistryObject<Item> FIRE_CLAY_PLATE = IPM.ITEMS.register(
            LibNames.FIRE_CLAY_PLATE, () -> new Item(defaultItemProperties())
    );
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
        return new Item.Properties().tab(CreativeTabHandler.MISC);
    }

    public static void init() {
    }
}
