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
import arekkuusu.implom.common.block.tile.TileAsymmetricalMachination;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

/*
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileAsymmetricalMachinationRenderer extends net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer<TileAsymmetricalMachination> {

	@Override
	public void render(TileAsymmetricalMachination te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		BakerLibrary.ASYMMETRICAL_MACHINATION_FRAME.renderWithRotation(facing);
		//Crystal
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BakerLibrary.ASYMMETRICAL_MACHINATION_CRYSTAL.renderWithRotation(facing);
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		//Gears
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		BakerLibrary.ASYMMETRICAL_MACHINATION_GEAR_TOP.renderWithRotation(facing, EnumFacing.Axis.Y, ((int)((tick) / 5F)) * 5F);
		BakerLibrary.ASYMMETRICAL_MACHINATION_GEAR_CENTER.renderWithRotation(facing, EnumFacing.Axis.Y, ((int)((tick) / 10F)) * -5F);
		BakerLibrary.ASYMMETRICAL_MACHINATION_GEAR_BOTTOM.renderWithRotation(facing, EnumFacing.Axis.Y, ((int)((tick) / 25F)) * 2.5F);
		GlStateManager.popMatrix();
	}
}
