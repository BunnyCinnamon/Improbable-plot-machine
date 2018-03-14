/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
}
