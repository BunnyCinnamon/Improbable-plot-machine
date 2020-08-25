package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.IPM;
import arekkuusu.implom.LibNames;
import arekkuusu.implom.common.block.ModBlocks;
import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.fml.RegistryObject;

public final class ModTiles {

    public static final RegistryObject<TileEntityType<TileMultiBlockImouto>> IMOUTO = IPM.TILES.register(
            "imouto", () -> register("imouto", TileEntityType.Builder.create(TileMultiBlockImouto::new, ModBlocks.FIRE_CLAY_BLOCK.get(), ModBlocks.FIRE_BRICK_BLOCK.get(), ModBlocks.FIRE_BRICKS.get(), ModBlocks.FIRE_BRICKS_GLASS.get(), ModBlocks.FIRE_BRICKS_WINDOW.get(), ModBlocks.BLAST_FURNACE_FILTER.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnaceController>> BLAST_FURNACE_CONTROLLER = IPM.TILES.register(
            LibNames.BLAST_FURNACE_CONTROLLER, () -> register(LibNames.BLAST_FURNACE_CONTROLLER, TileEntityType.Builder.create(TileBlastFurnaceController::new, ModBlocks.BLAST_FURNACE_CONTROLLER.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnaceDrain>> BLAST_FURNACE_DRAIN = IPM.TILES.register(
            LibNames.BLAST_FURNACE_DRAIN, () -> register(LibNames.BLAST_FURNACE_DRAIN, TileEntityType.Builder.create(TileBlastFurnaceDrain::new, ModBlocks.BLAST_FURNACE_DRAIN.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnaceInput>> BLAST_FURNACE_INPUT = IPM.TILES.register(
            LibNames.BLAST_FURNACE_INPUT, () -> register(LibNames.BLAST_FURNACE_INPUT, TileEntityType.Builder.create(TileBlastFurnaceInput::new, ModBlocks.BLAST_FURNACE_INPUT.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnaceTuyere>> BLAST_FURNACE_TUYERE = IPM.TILES.register(
            LibNames.BLAST_FURNACE_TUYERE, () -> register(LibNames.BLAST_FURNACE_TUYERE, TileEntityType.Builder.create(TileBlastFurnaceTuyere::new, ModBlocks.BLAST_FURNACE_TUYERE.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnacePipe>> BLAST_FURNACE_PIPE = IPM.TILES.register(
            LibNames.BLAST_FURNACE_PIPE, () -> register(LibNames.BLAST_FURNACE_PIPE, TileEntityType.Builder.create(TileBlastFurnacePipe::new, ModBlocks.BLAST_FURNACE_PIPE.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnaceAirVent>> BLAST_FURNACE_AIR_VENT = IPM.TILES.register(
            LibNames.BLAST_FURNACE_AIR_VENT, () -> register(LibNames.BLAST_FURNACE_AIR_VENT, TileEntityType.Builder.create(TileBlastFurnaceAirVent::new, ModBlocks.BLAST_FURNACE_AIR_VENT.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnaceAirPump>> BLAST_FURNACE_AIR_PUMP = IPM.TILES.register(
            LibNames.BLAST_FURNACE_AIR_PUMP, () -> register(LibNames.BLAST_FURNACE_AIR_PUMP, TileEntityType.Builder.create(TileBlastFurnaceAirPump::new, ModBlocks.BLAST_FURNACE_AIR_PUMP.get()))
    );

    public static <T extends TileEntity> TileEntityType<T> register(String key, TileEntityType.Builder<T> builder) {
        Type<?> type = Util.attemptDataFix(TypeReferences.BLOCK_ENTITY, key);
        return builder.build(type);
    }

    public static void init() {
    }
}
