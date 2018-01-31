/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.common.block.tile.TileQimranut;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Snack> on 30/01/2018.
 * It's distributed as part of Solar.
 */
public class QimranutRenderer extends SpecialModelRenderer<TileQimranut> {

	@Override
	void renderTile(TileQimranut te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(null, x, y, z, partialTicks);
	}

	private void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {

	}
}
