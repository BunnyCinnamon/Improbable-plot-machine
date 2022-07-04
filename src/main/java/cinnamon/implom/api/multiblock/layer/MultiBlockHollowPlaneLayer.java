package cinnamon.implom.api.multiblock.layer;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.List;

public class MultiBlockHollowPlaneLayer extends MultiBlockLayer {

	public MultiBlockHollowPlaneLayer(LayerData layerData) {
		super(layerData);
	}

	@Override
	public boolean detect(Level world, BlockPos center, int[] edges, List<BlockPos> candidates) {
		BlockPos from = center.offset(edges[1], 0, edges[2]);
		BlockPos to = center.offset(edges[3], 0, edges[0]);

		if(!world.hasChunksAt(from, to)) {
			return false;
		}

		// validate frame first
		if(layerData.hasFrame) {
			// calculate blocks
			List<BlockPos> frame = Lists.newArrayList();
			// x direction
			for(int x = 0; x <= to.getX() - from.getX(); x++) {
				frame.add(from.offset(x, 0, 0));
				frame.add(to.offset(-x, 0, 0));
			}
			// z direction. don't doublecheck corners
			for(int z = 1; z < to.getZ() - from.getZ(); z++) {
				frame.add(from.offset(0, 0, z));
				frame.add(to.offset(0, 0, -z));
			}

			// check the blocks
			for(BlockPos pos : frame) {
				if(!layerData.isValidBlock(LayerPiece.FRAME, world, pos)) {
					return false;
				}
				candidates.add(pos);
			}
		}

		// validate inside of the plane
		from = from.offset(1, 0, 1);
		to = to.offset(-1, 0, -1);

		boolean isHollow = Mth.sqrt((float) from.distSqr(to)) > 2; //Do we have a 2x2 structure?
		for(BlockPos z = from; z.getZ() <= to.getZ(); z = z.offset(0, 0, 1)) {
			for(BlockPos x = z; x.getX() <= to.getX(); x = x.offset(1, 0, 0)) {
				if(isHollow && center.equals(x)) { //Check center block
					if(!world.isEmptyBlock(x)) {
						return false;
					}
				}
				else if(!layerData.isValidBlock(LayerPiece.WALL ,world, x)) {
					return false;
				}
				candidates.add(x);
			}
		}

		return true;
	}
}
