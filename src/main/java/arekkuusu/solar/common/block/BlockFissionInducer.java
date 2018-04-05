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

/**
 * Created by <Arekkuusu> on 4/4/2018.
 * It's distributed as part of Solar.
 */
public class BlockFissionInducer extends BlockBaseFacing {

	public BlockFissionInducer() {
		super(LibNames.FISSION_INDUCER, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(5F);
		setResistance(2000F);
	}
}
