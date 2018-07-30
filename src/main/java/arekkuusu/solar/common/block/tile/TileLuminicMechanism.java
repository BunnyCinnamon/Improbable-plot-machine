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

	public static int REACH = 5;
	private int tick;

	@Override
	public void update() {
		if (world.isRemote && tick++ %  6 == 0) {
			EnumFacing facing = getFacingLazy().getOpposite();
			BlockPos.MutableBlockPos posOffset = new BlockPos.MutableBlockPos(pos);
			float distance = 0;
			while (distance++ < TileLuminicMechanism.REACH) {
				IBlockState found = world.getBlockState(posOffset.move(facing));
				if(found.getBlock() == ModBlocks.LUMEN_COMPRESSOR && found.getValue(BlockDirectional.FACING) == facing) {
					Vector3 offset = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable();
					Vector3 from = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D);
					FXUtil.addBeam(world, from, offset, distance, 36, 1.5F, 0xFFE077);
					break;
				}
			}
		}
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
