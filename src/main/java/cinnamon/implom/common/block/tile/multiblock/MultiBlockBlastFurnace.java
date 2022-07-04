package cinnamon.implom.common.block.tile.multiblock;

import cinnamon.implom.api.multiblock.MultiBlockRectanguloid;
import cinnamon.implom.api.multiblock.layer.Facing;
import cinnamon.implom.api.multiblock.layer.LayerPiece;
import cinnamon.implom.api.multiblock.layer.MultiBlockLayer.LayerData;
import cinnamon.implom.api.multiblock.layer.MultiBlockPlaneLayer;
import cinnamon.implom.api.multiblock.layer.MultiBlockWallLayer;
import cinnamon.implom.common.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;

import static cinnamon.implom.api.multiblock.layer.MultiBlockLayer.LayerData.of;
import static cinnamon.implom.api.multiblock.layer.MultiBlockLayer.LayerData.ofAir;

public class MultiBlockBlastFurnace extends MultiBlockRectanguloid {

    public MultiBlockBlastFurnace(int maxLength, WallType wallType) {
        super(maxLength, wallType);
        MultiBlockPlaneLayer bottom = new MultiBlockPlaneLayer(
                new LayerData(false, 1, 1)
                        .add(LayerPiece.WALL, of(ModBlocks.BLAST_FURNACE_DRAIN, ModBlocks.FIRE_BRICKS, ModBlocks.FIRE_BRICK_BLOCK))
        );
        insertLayer(bottom);
        MultiBlockWallLayer filter = new MultiBlockWallLayer(
                new LayerData(true, 1, 3)
                        .add(LayerPiece.INSIDE, of(
                                ModBlocks.BLAST_FURNACE_FILTER,
                                Blocks.AIR
                        ))
                        .with(ModBlocks.BLAST_FURNACE_FILTER, 1, Integer.MAX_VALUE, Facing.ANY)
                        .add(LayerPiece.WALL, of(
                                ModBlocks.FIRE_BRICKS,
                                ModBlocks.FIRE_BRICK_BLOCK,
                                ModBlocks.BLAST_FURNACE_DRAIN
                        ))
                        .add(LayerPiece.FRAME, of(ModBlocks.FIRE_BRICKS, ModBlocks.FIRE_BRICK_BLOCK))
        );
        insertLayer(filter);
        MultiBlockWallLayer controller = new MultiBlockWallLayer(
                new LayerData(false, 1, 1)
                        .add(LayerPiece.INSIDE, ofAir())
                        .add(LayerPiece.WALL, of(
                                ModBlocks.FIRE_BRICKS,
                                ModBlocks.FIRE_BRICK_BLOCK,
                                ModBlocks.FIRE_BRICKS_WINDOW,
                                ModBlocks.BLAST_FURNACE_CONTROLLER,
                                ModBlocks.BLAST_FURNACE_TUYERE
                        ))
                        .with(ModBlocks.BLAST_FURNACE_CONTROLLER, 1, 1, Facing.ANY)
                        .with(ModBlocks.BLAST_FURNACE_TUYERE, 1, Integer.MAX_VALUE, Facing.ANY)
        );
        insertLayer(controller);
        MultiBlockWallLayer coal = new MultiBlockWallLayer(
                new LayerData(false, 0, 3)
                        .add(LayerPiece.INSIDE, ofAir())
                        .add(LayerPiece.WALL, of(
                                ModBlocks.FIRE_BRICKS,
                                ModBlocks.FIRE_BRICK_BLOCK,
                                ModBlocks.FIRE_BRICKS_GLASS,
                                ModBlocks.FIRE_BRICKS_WINDOW,
                                ModBlocks.BLAST_FURNACE_TUYERE
                        ))
                        .with(ModBlocks.BLAST_FURNACE_TUYERE, 1, Integer.MAX_VALUE, Facing.ANY)
        );
        insertLayer(coal);
        MultiBlockWallLayer stone = new MultiBlockWallLayer(
                new LayerData(false, 1, 12)
                        .add(LayerPiece.INSIDE, ofAir())
                        .add(LayerPiece.WALL, of(
                                ModBlocks.FIRE_BRICKS,
                                ModBlocks.FIRE_BRICK_BLOCK,
                                ModBlocks.FIRE_BRICKS_GLASS,
                                ModBlocks.FIRE_BRICKS_WINDOW,
                                ModBlocks.BLAST_FURNACE_INPUT
                        ))
                        .with(ModBlocks.BLAST_FURNACE_INPUT, 1, 4, Facing.INWARDS_OUTWARDS)
        );
        insertLayer(stone);
        MultiBlockPlaneLayer top = new MultiBlockPlaneLayer(
                new LayerData(false, 0, 1)
                        .add(LayerPiece.WALL, of(ModBlocks.FIRE_BRICKS))
        );
        insertLayer(top);
    }
}
