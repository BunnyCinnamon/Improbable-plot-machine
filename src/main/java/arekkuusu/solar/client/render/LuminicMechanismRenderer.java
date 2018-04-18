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
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileLuminicMechanism;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 4/12/2018.
 * It's distributed as part of Solar.
 */
public class LuminicMechanismRenderer extends SpecialModelRenderer<TileLuminicMechanism> {

	@Override
	void renderTile(TileLuminicMechanism te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getFacingLazy(), x, y, z);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(EnumFacing.DOWN, x, y, z);
	}

	private void renderModel(@Nullable EnumFacing facing, double x, double y, double z) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		if(facing != null && facing != EnumFacing.DOWN) {
			switch(facing) {
				case UP:
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					break;
				case NORTH:
					GlStateManager.rotate(90F, 1F, 0F, 0F);
					break;
				case SOUTH:
					GlStateManager.rotate(90F, -1F, 0F, 0F);
					break;
				case WEST:
					GlStateManager.rotate(90F, 0F, 0F, -1F);
					break;
				case EAST:
					GlStateManager.rotate(90F, 0F, 0F, 1F);
					break;
			}
		}
		BlockBaker.render(BlockBaker.LUMINIC_MECHANISM);
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.render(BlockBaker.LUMINIC_MECHANISM_);
		GlStateManager.enableLighting();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.popMatrix();
	}
}
