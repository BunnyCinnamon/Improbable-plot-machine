/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block.tile;

import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Arekkuusu> on 5/3/2018.
 * It's distributed as part of Solar.
 */
public class TileLumenCompressor extends TileLumenBase {

	public static final int MAX_LUMEN = 64;

	@Override
	void onLumenChange() {
		//NO-OP
	}

	@Override
	ILumen createHandler() {
		return new LumenHandler(this) {
			@Override
			public int drain(int amount, boolean drain) {
				return 0;
			}
		};
	}

	@Override
	public int getCapacity() {
		return MAX_LUMEN;
	}

	public EnumFacing getFacingLazy() {
		return getStateValue(BlockDirectional.FACING, pos).orElse(EnumFacing.UP);
	}
}
