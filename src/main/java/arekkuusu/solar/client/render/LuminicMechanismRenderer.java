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
import arekkuusu.solar.common.block.tile.TileLuminicMechanism;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * Created by <Arekkuusu> on 4/12/2018.
 * It's distributed as part of Solar.
 */
public class LuminicMechanismRenderer extends TileEntitySpecialRenderer<TileLuminicMechanism> {

	@Override
	public void render(TileLuminicMechanism te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		switch(te.getFacingLazy()) {
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
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.LUMINIC_MECHANISM.render();
		GlStateManager.enableLighting();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.popMatrix();
	}
}
