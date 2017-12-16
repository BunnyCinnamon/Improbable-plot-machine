/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.material.FixedMaterial;
import arekkuusu.solar.client.util.helper.TooltipBuilder;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by <Arekkuusu> on 11/12/2017.
 * It's distributed as part of Solar.
 */
public class BlockMonolithic extends BlockBase {

	public BlockMonolithic() {
		super(LibNames.MONOLITHIC, FixedMaterial.DONT_MOVE);
		setTooltip(TooltipBuilder.inline().addI18("monolithic_description", TooltipBuilder.DARK_GRAY_ITALIC));
		setBlockUnbreakable();
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
		return false;
	}
}
