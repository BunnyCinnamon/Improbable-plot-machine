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
import arekkuusu.implom.common.block.tile.TileVacuumConveyor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

/*
 * Created by <Arekkuusu> on 07/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileVacuumConveyorRenderer extends net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer<TileVacuumConveyor> {

	@Override
	public void render(TileVacuumConveyor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		//Top
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		if(facing != null && facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
			GlStateManager.rotate(90F, facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? 1F : 0F, 0F, facing == EnumFacing.EAST || facing == EnumFacing.WEST ? 1F : 0F);
		}
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		BlockBaker.VACUUM_TOP.render();
		GlStateManager.popMatrix();
		//Middle
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.VACUUM_PIECE.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		//Bottom
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, -1F, 0F);
		BlockBaker.VACUUM_BOTTOM.render();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
