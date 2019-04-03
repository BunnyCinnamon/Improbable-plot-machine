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
import arekkuusu.implom.common.block.tile.TileMutator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

/*
 * Created by <Arekkuusu> on 30/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileMutatorRenderer extends TileEntitySpecialRenderer<TileMutator> {

	@Override
	public void render(TileMutator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		BakerLibrary.MUTATOR_FRAME.render();
		BakerLibrary.MUTATOR_FRAME_FRONT.renderWithRotation(facing);
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		//Overlay
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		BakerLibrary.MUTATOR_FRAME_OVERLAY.renderWithRotation(facing);
		BakerLibrary.MUTATOR_BOLTS.renderWithRotation(facing);
		BakerLibrary.MUTATOR_PLATE.renderWithRotation(facing, EnumFacing.Axis.Y, tick * 0.5F % 360F);
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
