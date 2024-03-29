package cinnamon.implom.api.multiblock.layer;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.util.List;

public class MultiBlockHollowWallLayer extends MultiBlockLayer {

	public MultiBlockHollowWallLayer(LayerData layerData) {
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
			// we only have 4 corner blocks to check
			frame.add(from);
			frame.add(to);
			frame.add(new BlockPos(to.getX(), from.getY(), from.getZ()));
			frame.add(new BlockPos(from.getX(), from.getY(), to.getZ()));

			// check the blocks
			for(BlockPos pos : frame) {
				if(!layerData.isValidBlock(LayerPiece.FRAME, world, pos)) {
					return false;
				}
				candidates.add(pos);
			}
		}

		// validate the inside
		List<BlockPos> blocks = Lists.newArrayList();
		boolean isHollow = Mth.sqrt((float) from.distSqr(to)) > 2; //Do we have a 2x2 structure?
		for(int x = edges[1] + 1; x < edges[3]; x++) {
			for(int z = edges[2] + 1; z < edges[0]; z++) {
				BlockPos pos = center.offset(x, 0, z);
				if(isHollow && center.equals(pos)) { //Check center block
					if(world.isEmptyBlock(pos)) {
						candidates.add(pos);
					}
					else {
						return false;
					}
				}
				else blocks.add(pos);
			}
		}
		for(BlockPos pos : blocks) {
			if(layerData.isValidBlock(LayerPiece.INSIDE, world, pos)) {
				candidates.add(pos);
			}
			else return false;
		}

		// validate the 4 sides
		blocks.clear();
		for(int x = edges[1] + 1; x < edges[3]; x++) {
			blocks.add(center.offset(x, 0, edges[2]));
			blocks.add(center.offset(x, 0, edges[0]));
		}
		for(int z = edges[2] + 1; z < edges[0]; z++) {
			blocks.add(center.offset(edges[1], 0, z));
			blocks.add(center.offset(edges[3], 0, z));
		}

		for(BlockPos pos : blocks) {
			if(!layerData.isValidBlock(LayerPiece.WALL, world, pos)) {
				return false;
			}
			candidates.add(pos);
		}

		return true;
	}
}
