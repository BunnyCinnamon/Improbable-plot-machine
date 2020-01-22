package arekkuusu.implom.common.handler.multiblock;

import arekkuusu.implom.api.multiblock.MultiblockRectanguloid;
import arekkuusu.implom.api.multiblock.layer.*;
import arekkuusu.implom.api.multiblock.layer.MultiblockLayer.LayerData;
import arekkuusu.implom.common.block.ModBlocks;
import net.minecraft.block.material.Material;

import static arekkuusu.implom.api.multiblock.layer.MultiblockLayer.LayerData.of;

public class MultiblockHotAir extends MultiblockRectanguloid {

	public MultiblockHotAir(int maxLength, WallType wallType) {
		super(maxLength, wallType);
		MultiblockPlaneLayer bottom = new MultiblockPlaneLayer(new LayerData(true, 1, 1)
				.add(LayerPiece.WALL, of(ModBlocks.HOT_BLAST_HEATER))
				.add(LayerPiece.FRAME, of(
						ModBlocks.FIRE_BRICKS,
						ModBlocks.FIRE_BRICK_BLOCK,
						ModBlocks.FIRE_BRICKS_WINDOW,
						ModBlocks.FIRE_BRICKS_GLASS
				)).with(ModBlocks.HOT_BLAST_HEATER, 1, 1, Direction.ANY)
		);
		insertLayer(bottom);
		MultiblockWallLayer fuel = new MultiblockWallLayer(new LayerData(true, 1, 1)
				.add(LayerPiece.INSIDE, of(
						Material.FIRE
				)).add(LayerPiece.WALL, of(
						ModBlocks.FIRE_BRICKS,
						ModBlocks.FIRE_BRICK_BLOCK,
						ModBlocks.FIRE_BRICKS_WINDOW,
						ModBlocks.FIRE_BRICKS_GLASS
				)).add(LayerPiece.FRAME, of(
						ModBlocks.FIRE_BRICKS,
						ModBlocks.FIRE_BRICK_BLOCK
				))
		);
		insertLayer(fuel);
		MultiblockHollowWallLayer pump = new MultiblockHollowWallLayer(new LayerData(true, 1, 5)
				.add(LayerPiece.WALL, of(
						ModBlocks.FIRE_BRICKS,
						ModBlocks.FIRE_BRICK_BLOCK,
						ModBlocks.FIRE_BRICKS_WINDOW,
						ModBlocks.FIRE_BRICKS_GLASS,
						ModBlocks.BLAST_FURNACE_AIR_PUMP
				)).add(LayerPiece.FRAME, of(
						ModBlocks.FIRE_BRICKS,
						ModBlocks.FIRE_BRICK_BLOCK,
						ModBlocks.FIRE_BRICKS_WINDOW,
						ModBlocks.FIRE_BRICKS_GLASS
				)).with(ModBlocks.BLAST_FURNACE_AIR_PUMP, 1, 5, Direction.INWARDS_OUTWARDS)
		);
		insertLayer(pump);
		MultiblockPlaneLayer top = new MultiblockPlaneLayer(new LayerData(true, 1, 1)
				.add(LayerPiece.WALL, of(ModBlocks.BLAST_FURNACE_AIR_VENT))
				.add(LayerPiece.FRAME, of(ModBlocks.FIRE_BRICKS, ModBlocks.FIRE_BRICK_BLOCK))
				.with(ModBlocks.BLAST_FURNACE_AIR_VENT, 1, 1, Direction.ANY)
		);
		insertLayer(top);
	}
}
