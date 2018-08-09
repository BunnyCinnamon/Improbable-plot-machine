/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.common.block.tile.TileKondenzator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Solar.
 */
public class KondenzatorRenderer extends SpecialModelRenderer<TileKondenzator> {

	@Override
	void renderTile(TileKondenzator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(null, x, y, z, partialTicks);
	}

	private void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		if(facing != null && facing != EnumFacing.UP) {
			switch(facing) {
				case DOWN:
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					break;
				case NORTH:
					GlStateManager.rotate(90F, -1F, 0F, 0F);
					break;
				case SOUTH:
					GlStateManager.rotate(90F, 1F, 0F, 0F);
					break;
				case WEST:
					GlStateManager.rotate(90F, 0F, 0F, 1F);
					break;
				case EAST:
					GlStateManager.rotate(90F, 0F, 0F, -1F);
					break;
			}
		}
		BlockBaker.KONDENZATOR_BASE.render();
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.KONDENZATOR_CENTER.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
