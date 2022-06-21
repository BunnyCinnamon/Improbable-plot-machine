package arekkuusu.implom.api.multiblock.layer;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class MultiBlockPlaneLayer extends MultiBlockLayer {

	public MultiBlockPlaneLayer(LayerData layerData) {
		super(layerData);
	}

	@Override
	public boolean detect(World world, BlockPos center, int[] edges, List<BlockPos> candidates) {
		BlockPos from = center.add(edges[1], 0, edges[2]);
		BlockPos to = center.add(edges[3], 0, edges[0]);

		if(!world.isAreaLoaded(from, to)) {
			return false;
		}

		// validate frame first
		if(layerData.hasFrame) {
			// calculate blocks
			List<BlockPos> frame = Lists.newArrayList();
			// x direction
			for(int x = 0; x <= to.getX() - from.getX(); x++) {
				frame.add(from.add(x, 0, 0));
				frame.add(to.add(-x, 0, 0));
			}
			// z direction. don't doublecheck corners
			for(int z = 1; z < to.getZ() - from.getZ(); z++) {
				frame.add(from.add(0, 0, z));
				frame.add(to.add(0, 0, -z));
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
		from = from.add(1, 0, 1);
		to = to.add(-1, 0, -1);

		for(BlockPos z = from; z.getZ() <= to.getZ(); z = z.add(0, 0, 1)) {
			for(BlockPos x = z; x.getX() <= to.getX(); x = x.add(1, 0, 0)) {
				if(!layerData.isValidBlock(LayerPiece.WALL, world, x)) {
					return false;
				}
				candidates.add(x);
			}
		}

		return true;
	}
}
