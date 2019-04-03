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
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		//Top
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		BakerLibrary.VACUUM_TOP.renderWithRotation(facing, EnumFacing.Axis.Y, tick * 0.5F % 360F);
		//Middle
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BakerLibrary.VACUUM_PLATE.renderWithRotation(facing);
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		//Bottom
		BakerLibrary.VACUUM_BOTTOM.renderWithRotation(facing, EnumFacing.Axis.Y, tick * -0.5F % 360F);
		GlStateManager.popMatrix();
	}
}
