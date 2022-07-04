package cinnamon.implom.common.block;

import cinnamon.implom.IPM;
import cinnamon.implom.LibNames;
import cinnamon.implom.common.block.fluid.ModFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {

    public static final RegistryObject<Block> HOT_AIR = IPM.BLOCKS.register(
            LibNames.HOT_AIR, () -> new LiquidBlock(ModFluids.HOT_AIR, Block.Properties.of(Material.WATER).strength(100.0F).noLootTable())
    );
    public static final RegistryObject<Block> LUMEN = IPM.BLOCKS.register(
            LibNames.LUMEN, () -> new LiquidBlock(ModFluids.LUMEN, Block.Properties.of(Material.LAVA).strength(100.0F).noLootTable())
    );
    public static final RegistryObject<Block> FIRE_CLAY_BLOCK = IPM.BLOCKS.register(
            LibNames.FIRE_CLAY_BLOCK, () -> new BlockBaseMultiBlock(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> FIRE_BRICK_BLOCK = IPM.BLOCKS.register(
            LibNames.FIRE_BRICK_BLOCK, () -> new BlockBaseMultiBlock(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> FIRE_BRICKS = IPM.BLOCKS.register(
            LibNames.FIRE_BRICKS, () -> new BlockBaseMultiBlock(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> FIRE_BRICKS_GLASS = IPM.BLOCKS.register(
            LibNames.FIRE_BRICKS_GLASS, () -> new BlockBaseMultiBlockGlass(Block.Properties.of(Material.GLASS))
    );
    public static final RegistryObject<Block> FIRE_BRICKS_WINDOW = IPM.BLOCKS.register(
            LibNames.FIRE_BRICKS_WINDOW, () -> new BlockBaseMultiBlockGlass(Block.Properties.of(Material.GLASS))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_CONTROLLER = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_CONTROLLER, () -> new BlockBlastFurnaceController(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_DRAIN = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_DRAIN, () -> new BlockBlastFurnaceDrain(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_FILTER = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_FILTER, () -> new BlockBlastFurnaceFilter(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_INPUT = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_INPUT, () -> new BlockBlastFurnaceInput(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_TUYERE = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_TUYERE, () -> new BlockBlastFurnaceTuyere(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_AIR_VENT = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_AIR_VENT, () -> new BlockHotBlastAirVent(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_AIR_PUMP = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_AIR_PUMP, () -> new BlockHotBlastAirPump(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_PIPE = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_PIPE, () -> new BlockBlastFurnacePipe(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_HEATER = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_HEATER, () -> new BlockBlastFurnaceHeater(Block.Properties.of(Material.STONE))
    );
    public static final RegistryObject<Block> BLAST_FURNACE_PIPE_GAUGE = IPM.BLOCKS.register(
            LibNames.BLAST_FURNACE_PIPE_GAUGE, () -> new BlockBlastFurnacePipeGauge(Block.Properties.of(Material.STONE))
    );

    public static void init() {
    }

    public enum Tool {
        PICK("pickaxe"),
        AXE("axe"),
        SHOVEL("shovel");

        final String name;

        Tool(String name) {
            this.name = name;
        }
    }

    public enum ToolLevel {
        WOOD_GOLD,
        STONE,
        IRON,
        DIAMOND
    }
}
