/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.entity.ai;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 05/08/2017.
 * It's distributed as part of Solar.
 */
public class FlightPathNavigate extends PathNavigate {

	public FlightPathNavigate(EntityLiving living, World worldIn) {
		super(living, worldIn);
	}

	@Override
	protected PathFinder getPathFinder() {
		return new PathFinder(new FlyingNodeProcessor());
	}

	@Override
	protected boolean canNavigate() {
		return !this.isInLiquid();
	}

	@Override
	protected Vec3d getEntityPosition() {
		return new Vec3d(this.entity.posX, this.entity.posY + (double) this.entity.height * 0.5D, this.entity.posZ);
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	protected void pathFollow() {
		Vec3d vec3d = this.getEntityPosition();
		float f = this.entity.width * this.entity.width;

		if(vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex())) < (double) f) {
			this.currentPath.incrementPathIndex();
		}

		for(int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
			Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.entity, j);

			if(vec3d1.squareDistanceTo(vec3d) <= 36.0D && this.isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0)) {
				this.currentPath.setCurrentPathIndex(j);
				break;
			}
		}

		this.checkForStuck(vec3d);
	}

	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
		RayTraceResult raytraceresult = this.world.rayTraceBlocks(posVec31, new Vec3d(posVec32.x, posVec32.y + (double) this.entity.height * 0.5D, posVec32.z), true, true, false);
		return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
	}

	@Override
	public boolean canEntityStandOnPos(BlockPos pos) {
		return !this.world.getBlockState(pos).isFullBlock();
	}

	//FIXME: It broke... after moving to a new forge version.
	private static class FlightNodeProcessor extends NodeProcessor {

		@Override
		public PathPoint getStart() {
			return this.openPoint(MathHelper.floor(this.entity.getEntityBoundingBox().minX), MathHelper.floor(this.entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(this.entity.getEntityBoundingBox().minZ));
		}

		@Override
		public PathPoint getPathPointToCoords(double x, double y, double z) {
			return this.openPoint(MathHelper.floor(x - (double) (this.entity.width / 2.0F)), MathHelper.floor(y + 0.5D), MathHelper.floor(z - (double) (this.entity.width / 2.0F)));
		}

		@Override
		public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint, PathPoint targetPoint, float maxDistance) {
			int i = 0;

			for(EnumFacing enumfacing : EnumFacing.values()) {
				PathPoint pathpoint = this.getAirNode(currentPoint.x + enumfacing.getFrontOffsetX(), currentPoint.y + enumfacing.getFrontOffsetY(), currentPoint.z + enumfacing.getFrontOffsetZ());

				if(pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
					pathOptions[i++] = pathpoint;
				}
			}

			return i;
		}

		@Override
		public PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z, EntityLiving living, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
			return PathNodeType.OPEN;
		}

		@Override
		public PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z) {
			return PathNodeType.OPEN;
		}

		@Nullable
		private PathPoint getAirNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
			PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
			return pathnodetype == PathNodeType.OPEN ? this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_) : null;
		}

		private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

			for(int i = p_186327_1_; i < p_186327_1_ + this.entitySizeX; ++i) {
				for(int j = p_186327_2_; j < p_186327_2_ + this.entitySizeY; ++j) {
					for(int k = p_186327_3_; k < p_186327_3_ + this.entitySizeZ; ++k) {
						IBlockState iblockstate = this.blockaccess.getBlockState(blockpos$mutableblockpos.setPos(i, j, k));

						if(iblockstate.getMaterial() != Material.AIR) {
							return PathNodeType.BLOCKED;
						}
					}
				}
			}

			return PathNodeType.OPEN;
		}
	}
}
