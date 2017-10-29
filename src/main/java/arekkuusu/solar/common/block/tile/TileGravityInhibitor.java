/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by <Arekkuusu> on 17/10/2017.
 * It's distributed as part of Solar.
 */
public class TileGravityInhibitor extends TileBase implements ITickable {

	private static final List<Entity> CANNOT_CAPTURE = Lists.newArrayList();
	private static final String WAS_CAPTURED = "was_captured";

	private final List<Entity> captured = Lists.newArrayList();
	private int range = 20;
	public int tick;

	@Override
	public void update() {
		if(!world.isRemote) {
			getFilteredEntities().forEach(this::captureEntity);
			filterCaptured();
		}
		tick++;
	}

	private void filterCaptured() {
		captured.removeIf(entity -> {
			entity.fallDistance = 0;

			if(entity.getDistanceSq(getPos()) > range || isOnGround(entity)) {
				freeEntity(entity);
				return true;
			}

			return entity.isDead;
		});
	}

	@Override
	public void invalidate() {
		super.invalidate();
		captured.forEach(this::freeEntity);
		CANNOT_CAPTURE.removeAll(captured);
	}

	private void captureEntity(Entity entity) {
		CANNOT_CAPTURE.add(entity);
		entity.getEntityData().setBoolean(WAS_CAPTURED, true);
		entity.setNoGravity(true);
		captured.add(entity);
	}

	private void freeEntity(Entity entity) {
		CANNOT_CAPTURE.remove(entity);
		entity.getEntityData().setBoolean(WAS_CAPTURED, false);
		entity.setNoGravity(false);
	}

	private List<Entity> getFilteredEntities() {
		return world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(getPos()).grow(range), entity ->
				entity != null && !isOnGround(entity) && !CANNOT_CAPTURE.contains(entity) &&
						(!entity.hasNoGravity() || entity.getEntityData().getBoolean(WAS_CAPTURED))
		);
	}

	private boolean isOnGround(Entity entity) {
		BlockPos down = entity.getPosition().down();
		IBlockState state = world.getBlockState(down);
		return state.isSideSolid(world, down, EnumFacing.UP);
	}

	public ImmutableList<Entity> getCaptured() {
		return ImmutableList.copyOf(captured);
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
		markDirty();
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		range = compound.getInteger("range");
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setInteger("range", range);
	}
}
