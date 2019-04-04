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
import arekkuusu.implom.common.block.tile.TileSymmetricalMachination;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;

/*
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileSymmetricalMachinationRenderer extends net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer<TileSymmetricalMachination> {

	@Override
	public void render(TileSymmetricalMachination te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		EnumFacing facing = te.getFacingLazy().getOpposite();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		switch (MinecraftForgeClient.getRenderPass()) {
			case 0:
				float tick = RenderHelper.getRenderWorldTime(partialTicks);
				BakerLibrary.SYMMETRICAL_MACHINATION_FRAME.renderWithRotation(facing);
				//Rings
				BakerLibrary.SYMMETRICAL_MACHINATION_RING.renderWithYOffset(facing, RenderHelper.getInterpolated(tick, 0.01F, 1.5F));
				break;
			case 1:
				//Inner core
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				ShaderLibrary.BRIGHT.begin();
				ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
					b.set(-0.2F);
					b.upload();
				});
				BakerLibrary.SYMMETRICAL_MACHINATION_CORE.renderWithRotation(facing);
				ShaderLibrary.BRIGHT.end();
				GlStateManager.enableLighting();
				GlStateManager.disableBlend();
				break;
		}
		GlStateManager.popMatrix();
	}
}
