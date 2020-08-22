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

import java.util.Objects;

public final class ModTiles {

    public static final RegistryObject<TileEntityType<TileMultiblockImouto>> IMOUTO = IPM.TILES.register(
            LibNames.FIRE_CLAY_BLOCK, () -> register(LibNames.FIRE_CLAY_BLOCK, TileEntityType.Builder.create(TileMultiblockImouto::new, ModBlocks.FIRE_CLAY_BLOCK.get(), ModBlocks.FIRE_BRICK_BLOCK.get(), ModBlocks.FIRE_BRICKS.get(), ModBlocks.FIRE_BRICKS_GLASS.get(), ModBlocks.FIRE_BRICKS_WINDOW.get()))
    );
    public static final RegistryObject<TileEntityType<TileBlastFurnaceController>> BLAST_FURNACE = IPM.TILES.register(
            LibNames.BLAST_FURNACE_CONTROLLER, () -> register(LibNames.BLAST_FURNACE_CONTROLLER, TileEntityType.Builder.create(TileBlastFurnaceController::new, ModBlocks.BLAST_FURNACE_CONTROLLER.get()))
    );

    public static <T extends TileEntity> TileEntityType<T> register(String key, TileEntityType.Builder<T> builder) {
        Type<?> type = Util.attemptDataFix(TypeReferences.BLOCK_ENTITY, key);
        return builder.build(Objects.requireNonNull(type));
    }

    public static void init() {
    }
}
