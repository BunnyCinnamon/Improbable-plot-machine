package arekkuusu.implom.common.handler.multiblock;

import arekkuusu.implom.api.multiblock.MultiblockRectanguloid;
import arekkuusu.implom.api.multiblock.layer.*;
import arekkuusu.implom.api.multiblock.layer.MultiblockLayer.LayerData;
import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.init.Blocks;

import static arekkuusu.implom.api.multiblock.layer.MultiblockLayer.LayerData.of;
import static arekkuusu.implom.api.multiblock.layer.MultiblockLayer.LayerData.ofAir;

public class MultiblockBlastFurnace extends MultiblockRectanguloid {

	public MultiblockBlastFurnace(int maxLength, WallType wallType) {
		super(maxLength, wallType);
		MultiblockPlaneLayer bottom = new MultiblockPlaneLayer(
				new LayerData(false, 1, 1)
						.add(LayerPiece.WALL, of(ModBlocks.BLAST_FURNACE_DRAIN, ModBlocks.FIRE_BRICKS, ModBlocks.FIRE_BRICK_BLOCK))
		);
		insertLayer(bottom);
		MultiblockWallLayer filter = new MultiblockWallLayer(
				new LayerData(true, 1, 3)
						.add(LayerPiece.INSIDE, of(
								ModBlocks.BLAST_FURNACE_FILTER,
								Blocks.AIR
						))
						.with(ModBlocks.BLAST_FURNACE_FILTER, 1, Integer.MAX_VALUE, Direction.ANY)
						.add(LayerPiece.WALL, of(
								ModBlocks.FIRE_BRICKS,
								ModBlocks.FIRE_BRICK_BLOCK,
								ModBlocks.BLAST_FURNACE_DRAIN
						))
						.add(LayerPiece.FRAME, of(ModBlocks.FIRE_BRICKS, ModBlocks.FIRE_BRICK_BLOCK))
		);
		insertLayer(filter);
		MultiblockWallLayer controller = new MultiblockWallLayer(
				new LayerData(true, 1, 1)
						.add(LayerPiece.INSIDE, ofAir())
						.add(LayerPiece.WALL, of(
								ModBlocks.FIRE_BRICKS,
								ModBlocks.FIRE_BRICK_BLOCK,
								ModBlocks.FIRE_BRICKS_WINDOW,
								ModBlocks.BLAST_FURNACE_CONTROLLER,
								ModBlocks.BLAST_FURNACE_TUYERE
						))
						.with(ModBlocks.BLAST_FURNACE_CONTROLLER, 1, 1, Direction.ANY)
						.with(ModBlocks.BLAST_FURNACE_TUYERE, 1, Integer.MAX_VALUE, Direction.ANY)
						.add(LayerPiece.FRAME, of(ModBlocks.FIRE_BRICKS, ModBlocks.FIRE_BRICK_BLOCK))
		);
		insertLayer(controller);
		MultiblockWallLayer coal = new MultiblockWallLayer(
				new LayerData(false, 0, 3)
						.add(LayerPiece.INSIDE, ofAir())
						.add(LayerPiece.WALL, of(
								ModBlocks.FIRE_BRICKS,
								ModBlocks.FIRE_BRICK_BLOCK,
								ModBlocks.FIRE_BRICKS_GLASS,
								ModBlocks.FIRE_BRICKS_WINDOW,
								ModBlocks.BLAST_FURNACE_TUYERE
						))
						.with(ModBlocks.BLAST_FURNACE_TUYERE, 1, Integer.MAX_VALUE, Direction.ANY)
		);
		insertLayer(coal);
		MultiblockWallLayer stone = new MultiblockWallLayer(
				new LayerData(false, 1, 12)
						.add(LayerPiece.INSIDE, ofAir())
						.add(LayerPiece.WALL, of(
								ModBlocks.FIRE_BRICKS,
								ModBlocks.FIRE_BRICK_BLOCK,
								ModBlocks.FIRE_BRICKS_GLASS,
								ModBlocks.FIRE_BRICKS_WINDOW,
								ModBlocks.BLAST_FURNACE_INPUT
						))
						.with(ModBlocks.BLAST_FURNACE_INPUT, 1, 4, Direction.INWARDS_OUTWARDS)
		);
		insertLayer(stone);
		MultiblockPlaneLayer top = new MultiblockPlaneLayer(
				new LayerData(false, 0, 1)
						.add(LayerPiece.WALL, of(ModBlocks.FIRE_BRICKS))
		);
		insertLayer(top);
	}
}
