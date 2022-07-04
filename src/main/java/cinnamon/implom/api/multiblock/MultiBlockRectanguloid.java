package cinnamon.implom.api.multiblock;

import cinnamon.implom.api.multiblock.layer.LayerPiece;
import cinnamon.implom.api.multiblock.layer.MultiBlockLayer;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class MultiBlockRectanguloid extends MultiBlockDetector {

	public final List<MultiBlockLayer> layers = Lists.newLinkedList();
	public final WallType wallType;
	public final int maxLength;

	public MultiBlockRectanguloid(int maxLength, WallType wallType) {
		this.maxLength = maxLength;
		this.wallType = wallType;
	}

	public void insertLayer(MultiBlockLayer layer) {
		layers.add(layer);
	}

	@Override
	@Nullable
	public MultiblockStructure detectMultiBlock(Level world, BlockPos center) {
		List<BlockPos> subBlocks = Lists.newArrayList();

		// move as low as possible
		int masterY = center.getY();
		center = getOuterPos(world, center, Direction.DOWN, 64).above();
		center = detectCenter(world, center, maxLength);

		// below lowest internal position
		if(masterY < center.getY()) {
			return null;
		}

		// distances to the edges including the outer blocks
		int[] edges = new int[4];
		// order: south/west/north/east
		for(Direction direction : Direction.Plane.HORIZONTAL) {
			// move to wall
			BlockPos pos = getOuterPos(world, center, direction, maxLength);

			edges[direction.get2DDataValue()] = (pos.getX() - center.getX()) + (pos.getZ() - center.getZ());
		}

		// walls too far away?
		int xd = (edges[Direction.SOUTH.get2DDataValue()] - edges[Direction.NORTH.get2DDataValue()]) - 1;
		int zd = (edges[Direction.EAST.get2DDataValue()] - edges[Direction.WEST.get2DDataValue()]) - 1;
		if(xd > maxLength || zd > maxLength) {
			return null;
		}
		switch (wallType) {
			case EVEN:
				if(xd % 2 != 0) return null;
				break;
			case ODD:
				if(xd % 2 == 0) return null;
				break;
			default:
		}
		center = center.below();
		int height = 0;
		for(Iterator<MultiBlockLayer> iterator = layers.iterator(); iterator.hasNext(); ) {
			List<BlockPos> candidates = Lists.newArrayList();
			MultiBlockLayer layer = iterator.next();
			int maxLayerHeight = layer.layerData.maxHeight;
			int minLayerHeight = layer.layerData.minHeight;
			int layerHeight = 0;
			for(; height + center.getY() < world.getHeight(); height++, layerHeight++) {
				List<BlockPos> temp = Lists.newArrayList();
				if(!layer.detect(world, center.above(height), edges, temp)) {
					if(layerHeight >= minLayerHeight) {
						break;
					}
					else return null;
				} else candidates.addAll(temp);
			}
			if((!iterator.hasNext() && layerHeight > maxLayerHeight) || layerHeight < minLayerHeight) {
				return null;
			}
			if(layerHeight > 0 && !layer.validate(world, candidates)) {
				if(minLayerHeight == 0) {
					height -= layerHeight;
					continue;
				}
				else return null;
			}
			subBlocks.addAll(candidates);
		}
		if(height < 1 + masterY - center.getY()) {
			return null;
		}
		return new MultiblockStructure(xd, height, zd, subBlocks);
	}

	@Override
	public boolean isInside(Level world, BlockPos pos) {
		return world.isLoaded(pos) && layers.stream().noneMatch(layer -> layer.layerData.isValidBlock(LayerPiece.WALL, world, pos));
	}

	public enum WallType {
		EVEN,
		ODD,
		ANY
	}
}
