/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Solar.
 */
public class TileQimranut extends TileSimpleLinkBase {

	@Nullable
	public <T> T accessCapability(Capability<T> capability) {
		BlockPos offset = pos.offset(getFacingLazy());
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock().hasTileEntity(state)) {
			TileEntity tile = world.getTileEntity(offset);
			return tile != null && !(tile instanceof TileQimranut) ? tile.getCapability(capability, getFacingLazy()) : null;
		}
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getInverse().filter(t -> t.isLoaded() && t instanceof TileQimranut)
				.map(t -> ((TileQimranut) t).accessCapability(capability) != null)
				.orElse(super.hasCapability(capability, facing)) : false;
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getInverse().filter(t -> t.isLoaded() && t instanceof TileQimranut)
				.map(t -> ((TileQimranut) t).accessCapability(capability))
				.orElse(super.getCapability(capability, facing)) : null;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
