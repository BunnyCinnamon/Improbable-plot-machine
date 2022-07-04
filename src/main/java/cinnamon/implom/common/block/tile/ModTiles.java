package cinnamon.implom.common.block.tile;

import cinnamon.implom.IPM;
import cinnamon.implom.LibNames;
import cinnamon.implom.common.block.ModBlocks;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public final class ModTiles {

    public static final RegistryObject<BlockEntityType<TileMultiBlockImouto>> IMOUTO = IPM.TILES.register(
            "imouto", () -> register("imouto", BlockEntityType.Builder.of(TileMultiBlockImouto::new, ModBlocks.FIRE_CLAY_BLOCK.get(), ModBlocks.FIRE_BRICK_BLOCK.get(), ModBlocks.FIRE_BRICKS.get(), ModBlocks.FIRE_BRICKS_GLASS.get(), ModBlocks.FIRE_BRICKS_WINDOW.get(), ModBlocks.BLAST_FURNACE_FILTER.get()))
    );
    public static final RegistryObject<BlockEntityType<TileBlastFurnaceController>> BLAST_FURNACE_CONTROLLER = IPM.TILES.register(
            LibNames.BLAST_FURNACE_CONTROLLER, () -> register(LibNames.BLAST_FURNACE_CONTROLLER, BlockEntityType.Builder.of(TileBlastFurnaceController::new, ModBlocks.BLAST_FURNACE_CONTROLLER.get()))
    );
    public static final RegistryObject<BlockEntityType<TileBlastFurnaceDrain>> BLAST_FURNACE_DRAIN = IPM.TILES.register(
            LibNames.BLAST_FURNACE_DRAIN, () -> register(LibNames.BLAST_FURNACE_DRAIN, BlockEntityType.Builder.of(TileBlastFurnaceDrain::new, ModBlocks.BLAST_FURNACE_DRAIN.get()))
    );
    public static final RegistryObject<BlockEntityType<TileBlastFurnaceInput>> BLAST_FURNACE_INPUT = IPM.TILES.register(
            LibNames.BLAST_FURNACE_INPUT, () -> register(LibNames.BLAST_FURNACE_INPUT, BlockEntityType.Builder.of(TileBlastFurnaceInput::new, ModBlocks.BLAST_FURNACE_INPUT.get()))
    );
    public static final RegistryObject<BlockEntityType<TileBlastFurnaceTuyere>> BLAST_FURNACE_TUYERE = IPM.TILES.register(
            LibNames.BLAST_FURNACE_TUYERE, () -> register(LibNames.BLAST_FURNACE_TUYERE, BlockEntityType.Builder.of(TileBlastFurnaceTuyere::new, ModBlocks.BLAST_FURNACE_TUYERE.get()))
    );
    public static final RegistryObject<BlockEntityType<TileBlastFurnacePipe>> BLAST_FURNACE_PIPE = IPM.TILES.register(
            LibNames.BLAST_FURNACE_PIPE, () -> register(LibNames.BLAST_FURNACE_PIPE, BlockEntityType.Builder.of(TileBlastFurnacePipe::new, ModBlocks.BLAST_FURNACE_PIPE.get()))
    );
    public static final RegistryObject<BlockEntityType<TileBlastFurnaceAirVent>> BLAST_FURNACE_AIR_VENT = IPM.TILES.register(
            LibNames.BLAST_FURNACE_AIR_VENT, () -> register(LibNames.BLAST_FURNACE_AIR_VENT, BlockEntityType.Builder.of(TileBlastFurnaceAirVent::new, ModBlocks.BLAST_FURNACE_AIR_VENT.get()))
    );
    public static final RegistryObject<BlockEntityType<TileBlastFurnaceAirPump>> BLAST_FURNACE_AIR_PUMP = IPM.TILES.register(
            LibNames.BLAST_FURNACE_AIR_PUMP, () -> register(LibNames.BLAST_FURNACE_AIR_PUMP, BlockEntityType.Builder.of(TileBlastFurnaceAirPump::new, ModBlocks.BLAST_FURNACE_AIR_PUMP.get()))
    );

    public static <T extends BlockEntity> BlockEntityType<T> register(String key, BlockEntityType.Builder<T> builder) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, key);
        return builder.build(type);
    }

    public static void init() {
    }
}
