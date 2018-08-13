/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.common.block.ModBlocks;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

/**
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Solar.
 */
public class TileLuminicMechanism extends TileBase implements ITickable {

	public static final int REACH = 5;

	@Override
	public void update() {
		if(world.getTotalWorldTime() % 20 == 0) {
			EnumFacing facing = getFacingLazy().getOpposite();
			if(world.getBlockState(pos.offset(facing.getOpposite())).getBlock() == ModBlocks.PHOTON_CONTAINER) {
				BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(pos);
				float distance = 0;
				while(distance++ < TileLuminicMechanism.REACH) {
					IBlockState found = world.getBlockState(posOffset.move(facing));
					if(found.getBlock() == ModBlocks.LUMEN_COMPRESSOR && found.getValue(BlockDirectional.FACING) == facing) {
						if(!world.isRemote) {
							transfer(posOffset.toImmutable());
						} else {
							makeParticles(facing);
						}
						break;
					} else if(!found.getBlock().isReplaceable(world, posOffset)) break;
				}
			}
		}
	}

	private void transfer(BlockPos pos) {

	}

	private void makeParticles(EnumFacing facing) {
		Vector3 offset = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable().multiply(0.05D);
		Vector3 from = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D);
		FXUtil.spawnNeutron(world, from, offset, 120, 1F, 0xFFE077, true);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
