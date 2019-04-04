/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.BakerLibrary;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileMechanicalTranslocator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

/*
 * Created by <Arekkuusu> on 21/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileMechanicalTranslocatorRenderer extends TileEntitySpecialRenderer<TileMechanicalTranslocator> {

	@Override
	public void render(TileMechanicalTranslocator translocator, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(translocator.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		//Top & Bottom
		BakerLibrary.TRANSLOCATOR_FRAME.renderWithRotation(facing, EnumFacing.Axis.Y, tick * 0.5F % 360F);
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		//Middle
		BakerLibrary.TRANSLOCATOR_PLATE.renderWithRotation(facing, EnumFacing.Axis.Y, tick * -0.5F % 360F);
		//Piece
		/*--- Outwards ---*/
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5, 0.5, 0.5);
		GlStateManager.scale(0.425F, 0.425F, 0.425F);
		GlStateManager.rotate(tick * 0.75F % 360F, 0F, 1F, 0F);
		GlStateManager.rotate(tick * 0.75F % 720F, 1F, 0F, 0F);
		GlStateManager.rotate(tick * 0.75F % 360F, 0F, 0F, 1F);
		GlStateManager.translate(-0.5, -0.5, -0.5);
		BakerLibrary.TRANSLOCATOR_RING.renderWithYOffset(facing, RenderHelper.getInterpolated(tick, 0.05F, 1F));
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5, 0.5, 0.5);
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		GlStateManager.rotate(tick % 360F, 0F, -1F, 0F);
		GlStateManager.rotate(tick % 720F, -1F, 0F, 0F);
		GlStateManager.rotate(tick % 360F, 0F, 0F, -1F);
		GlStateManager.translate(-0.5, -0.5, -0.5);
		BakerLibrary.TRANSLOCATOR_RING.renderWithYOffset(facing, RenderHelper.getInterpolated(tick, 0.05F, 1F));
		GlStateManager.popMatrix();
		/*--- Inwards ---*/
		GlStateManager.enableLighting();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.popMatrix();
	}
}
