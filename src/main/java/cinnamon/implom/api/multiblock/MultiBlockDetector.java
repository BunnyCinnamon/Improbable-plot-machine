package cinnamon.implom.api.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;

//From tinkers construct
public abstract class MultiBlockDetector {

	public static class MultiblockStructure {

		public final int x;
		public final int y;
		public final int z;
		public final List<BlockPos> blocks;
		public final BlockPos minPos;
		public final BlockPos maxPos;

		protected final AABB bb;

		public MultiblockStructure(int xd, int yd, int zd, List<BlockPos> blocks) {
			this.x = xd;
			this.y = yd;
			this.z = zd;
			this.blocks = blocks;

			int minx = Integer.MAX_VALUE;
			int maxx = Integer.MIN_VALUE;
			int miny = Integer.MAX_VALUE;
			int maxy = Integer.MIN_VALUE;
			int minz = Integer.MAX_VALUE;
			int maxz = Integer.MIN_VALUE;
			for(BlockPos pos : blocks) {
				if(pos.getX() < minx) {
					minx = pos.getX();
				}
				if(pos.getX() > maxx) {
					maxx = pos.getX();
				}
				if(pos.getY() < miny) {
					miny = pos.getY();
				}
				if(pos.getY() > maxy) {
					maxy = pos.getY();
				}
				if(pos.getZ() < minz) {
					minz = pos.getZ();
				}
				if(pos.getZ() > maxz) {
					maxz = pos.getZ();
				}
			}

			bb = new AABB(minx, miny, minz, maxx + 1, maxy + 1, maxz + 1);
			minPos = new BlockPos(minx, miny, minz);
			maxPos = new BlockPos(maxx, maxy, maxz);
		}

		public AABB getBoundingBox() {
			return bb;
		}
	}

	public BlockPos detectCenter(Level world, BlockPos inside, int limit) {
		int xd1 = 1, xd2 = 1;
		int zd1 = 1, zd2 = 1;
		for(int i = 1; i < limit; i++) {
			if(isInside(world, inside.offset(-xd1, 0, 0))) {
				xd1++;
			}
			else if(isInside(world, inside.offset(xd2, 0, 0))) {
				xd2++;
			}

			if(xd1 - xd2 > 1) {
				xd1--;
				inside = inside.offset(-1, 0, 0);
				xd2++;
			}
			if(xd2 - xd1 > 1) {
				xd2--;
				inside = inside.offset(1, 0, 0);
				xd1++;
			}

			if(isInside(world, inside.offset(0, 0, -zd1))) {
				zd1++;
			}
			else if(isInside(world, inside.offset(0, 0, zd2))) {
				zd2++;
			}

			if(zd1 - zd2 > 1) {
				zd1--;
				inside = inside.offset(0, 0, -1);
				zd2++;
			}
			if(zd2 - zd1 > 1) {
				zd2--;
				inside = inside.offset(0, 0, 1);
				zd1++;
			}
		}

		return inside;
	}

	public BlockPos getOuterPos(Level world, BlockPos pos, Direction direction, int limit) {
		for(int i = 0; i < limit && isInside(world, pos); i++) {
			pos = pos.relative(direction);
		}

		return pos;
	}

	public abstract boolean isInside(Level world, BlockPos pos);

	public abstract MultiblockStructure detectMultiBlock(Level world, BlockPos center);

	public boolean checkCanStructureBeDetected(Level world, MultiblockStructure structure) {
		return structure != null && structure.minPos.distSqr(structure.maxPos) > 1 && world.hasChunksAt(structure.minPos, structure.maxPos);
	}

	public static void assignMultiBlockServants(Level world, BlockPos master, List<BlockPos> servants) {
		for(BlockPos pos : servants) {
			if(world.isLoaded(pos)) {
				BlockEntity slave = world.getBlockEntity(pos);
				if(slave instanceof MultiBlockImouto) {
					MultiBlockImouto imouto = (MultiBlockImouto) slave;
					imouto.setOniichan(master);
				}
			}
		}
	}
}
