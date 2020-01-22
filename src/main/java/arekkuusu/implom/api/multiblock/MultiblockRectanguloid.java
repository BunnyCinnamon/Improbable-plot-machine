package arekkuusu.implom.api.multiblock;

import arekkuusu.implom.api.multiblock.layer.LayerPiece;
import arekkuusu.implom.api.multiblock.layer.MultiblockLayer;
import com.google.common.collect.Lists;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class MultiblockRectanguloid extends MultiblockDetector {

	public final List<MultiblockLayer> layers = Lists.newLinkedList();
	public final WallType wallType;
	public final int maxLength;

	public MultiblockRectanguloid(int maxLength, WallType wallType) {
		this.maxLength = maxLength;
		this.wallType = wallType;
	}

	public void insertLayer(MultiblockLayer layer) {
		layers.add(layer);
	}

	@Override
	@Nullable
	public MultiblockStructure detectMultiblock(World world, BlockPos center) {
		List<BlockPos> subBlocks = Lists.newArrayList();

		// move as low as possible
		int masterY = center.getY();
		center = getOuterPos(world, center, EnumFacing.DOWN, 64).up();
		center = detectCenter(world, center, maxLength);

		// below lowest internal position
		if(masterY < center.getY()) {
			return null;
		}

		// distances to the edges including the outer blocks
		int[] edges = new int[4];
		// order: south/west/north/east
		for(EnumFacing direction : EnumFacing.HORIZONTALS) {
			// move to wall
			BlockPos pos = getOuterPos(world, center, direction, maxLength);

			edges[direction.getHorizontalIndex()] = (pos.getX() - center.getX()) + (pos.getZ() - center.getZ());
		}

		// walls too far away?
		int xd = (edges[EnumFacing.SOUTH.getHorizontalIndex()] - edges[EnumFacing.NORTH.getHorizontalIndex()]) - 1;
		int zd = (edges[EnumFacing.EAST.getHorizontalIndex()] - edges[EnumFacing.WEST.getHorizontalIndex()]) - 1;
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
		center = center.down();
		int height = 0;
		for(Iterator<MultiblockLayer> iterator = layers.iterator(); iterator.hasNext(); ) {
			List<BlockPos> candidates = Lists.newArrayList();
			MultiblockLayer layer = iterator.next();
			int maxLayerHeight = layer.layerData.maxHeight;
			int minLayerHeight = layer.layerData.minHeight;
			int layerHeight = 0;
			for(; height + center.getY() < world.getHeight(); height++, layerHeight++) {
				List<BlockPos> temp = Lists.newArrayList();
				if(!layer.detect(world, center.up(height), edges, temp)) {
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
	public boolean isInside(World world, BlockPos pos) {
		return world.isBlockLoaded(pos) && layers.stream().noneMatch(layer -> layer.layerData.isValidBlock(LayerPiece.WALL, world, pos));
	}

	public enum WallType {
		EVEN,
		ODD,
		ANY
	}
}
