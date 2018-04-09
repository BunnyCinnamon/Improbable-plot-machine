/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/9/2018.
 * It's distributed as part of Solar.
 */
public class BlockLuminicMechanism extends BlockBaseFacing {

	public BlockLuminicMechanism() {
		super(LibNames.LUMINIC_MECHANISM, FixedMaterial.DONT_MOVE);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return null;
	}
}
