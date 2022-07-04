package cinnamon.implom.common.block.tile.multiblock;

import cinnamon.implom.api.multiblock.MultiBlockRectanguloid;
import cinnamon.implom.api.multiblock.layer.*;
import cinnamon.implom.common.block.ModBlocks;
import net.minecraft.world.level.material.Material;

import static cinnamon.implom.api.multiblock.layer.MultiBlockLayer.LayerData.of;

public class MultiBlockHotAir extends MultiBlockRectanguloid {

	public MultiBlockHotAir(int maxLength, WallType wallType) {
		super(maxLength, wallType);
		MultiBlockPlaneLayer bottom = new MultiBlockPlaneLayer(new MultiBlockLayer.LayerData(true, 1, 1)
				.add(LayerPiece.WALL, of(ModBlocks.BLAST_FURNACE_HEATER))
				.add(LayerPiece.FRAME, of(
						ModBlocks.FIRE_BRICKS,
						ModBlocks.FIRE_BRICK_BLOCK,
						ModBlocks.FIRE_BRICKS_WINDOW,
						ModBlocks.FIRE_BRICKS_GLASS
				)).with(ModBlocks.BLAST_FURNACE_HEATER, 1, 1, Facing.ANY)
		);
		insertLayer(bottom);
		MultiBlockWallLayer fuel = new MultiBlockWallLayer(new MultiBlockLayer.LayerData(true, 1, 1)
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
		MultiBlockHollowWallLayer pump = new MultiBlockHollowWallLayer(new MultiBlockLayer.LayerData(true, 1, 5)
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
				)).with(ModBlocks.BLAST_FURNACE_AIR_PUMP, 1, 5, Facing.INWARDS_OUTWARDS)
		);
		insertLayer(pump);
		MultiBlockPlaneLayer top = new MultiBlockPlaneLayer(new MultiBlockLayer.LayerData(true, 1, 1)
				.add(LayerPiece.WALL, of(ModBlocks.BLAST_FURNACE_AIR_VENT))
				.add(LayerPiece.FRAME, of(ModBlocks.FIRE_BRICKS, ModBlocks.FIRE_BRICK_BLOCK))
				.with(ModBlocks.BLAST_FURNACE_AIR_VENT, 1, 1, Facing.ANY)
		);
		insertLayer(top);
	}
}
