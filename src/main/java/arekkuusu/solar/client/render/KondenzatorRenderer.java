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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

/**
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Solar.
 */
public class KondenzatorRenderer extends SpecialModelRenderer<TileKondenzator> {

	@Override
	void renderTile(TileKondenzator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getLumen(), te.getFacingLazy(), x, y, z);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(TileKondenzator.MAX_LUMEN, null, x, y, z);
	}

	private void renderModel(int neutrons, EnumFacing facing, double x, double y, double z) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
			float brightness = (float) neutrons / (float) TileKondenzator.MAX_LUMEN;
			b.set(-0.65F + brightness * 0.65F);
			b.upload();
		});
		BlockBaker.KONDENZATOR_CENTER.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
