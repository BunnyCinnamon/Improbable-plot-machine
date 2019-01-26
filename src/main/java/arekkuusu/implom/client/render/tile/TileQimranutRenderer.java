/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileQimranut;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

/*
 * Created by <Arekkuusu> on 30/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileQimranutRenderer extends TileEntitySpecialRenderer<TileQimranut> {

	@Override
	public void render(TileQimranut te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
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
		//Base
		BlockBaker.QIMRANUT.render();
		//Piece
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.QIMRANUT_RING.render();
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, -1F, 0F);
		BlockBaker.QIMRANUT_.render();
		GlStateManager.popMatrix();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
