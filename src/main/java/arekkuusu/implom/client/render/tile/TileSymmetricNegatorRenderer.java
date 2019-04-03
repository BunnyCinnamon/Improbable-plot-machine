/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.api.helper.MathHelper;
import arekkuusu.implom.client.util.BakerLibrary;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileSymmetricNegator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;

/*
 * Created by <Arekkuusu> on 5/13/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileSymmetricNegatorRenderer extends net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer<TileSymmetricNegator> {

	@Override
	public void render(TileSymmetricNegator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		EnumFacing facing = te.getFacingLazy().getOpposite();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		switch (MinecraftForgeClient.getRenderPass()) {
			case 0:
				float tick = RenderHelper.getRenderWorldTime(partialTicks);
				BakerLibrary.SYMMETRIC_SENDER_FRAME.renderWithRotation(facing);
				//Rings
				BakerLibrary.SYMMETRIC_SENDER_RING.renderWithYOffset(facing, MathHelper.getInterpolated(tick, 0.01F, 1.5F));
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
				BakerLibrary.SYMMETRIC_SENDER_CORE.renderWithRotation(facing);
				ShaderLibrary.BRIGHT.end();
				GlStateManager.enableLighting();
				GlStateManager.disableBlend();
				break;
		}
		GlStateManager.popMatrix();
	}
}
