/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.common.block.tile.TileDilaton;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by <Snack> on 04/02/2018.
 * It's distributed as part of Solar.
 */
public class BlockDilaton extends BlockBase {

	public BlockDilaton() {
		super(LibNames.DILATON, FixedMaterial.DONT_MOVE);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileDilaton();
	}
}
