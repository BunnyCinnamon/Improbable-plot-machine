/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block.tile;

import net.minecraft.block.BlockDirectional;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.Optional;

/*
 * Created by <Arekkuusu> on 23/12/2017.
 * It's distributed as part of Solar.
 */
public class TileQimranut extends TileWorldAccessBase {

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getTargetTile()
				.map(tile -> tile.hasCapability(capability, handler.getFacing().orElse(null)))
				.orElse(false) : super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		return getFacingLazy() == facing ? getTargetTile()
				.map(tile -> tile.getCapability(capability, handler.getFacing().orElse(null)))
				.orElse(null) : super.getCapability(capability, facing);
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}

	public Optional<TileEntity> getTargetTile() {
		return handler.getData()
				.filter(data -> data.getPos() != null && data.getWorld() != null)
				.flatMap(data -> getTile(TileEntity.class, data.getWorld(), data.getPos())
						.filter(tile -> !(tile instanceof TileQimranut))
				);
	}
}
