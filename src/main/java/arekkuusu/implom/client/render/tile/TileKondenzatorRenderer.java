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
import arekkuusu.implom.common.block.BlockKondenzator;
import arekkuusu.implom.common.block.tile.TileKondenzator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

/*
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileKondenzatorRenderer extends TileEntitySpecialRenderer<TileKondenzator> {

	@Override
	public void render(TileKondenzator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, te.lumenCapability.get());
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, int neutrons) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		BakerLibrary.KONDENZATOR_FRAME.renderWithRotation(facing);
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			float brightness = (float) neutrons / (float) BlockKondenzator.ImbuingConstants.IMBUING_CAPACITY;
			b.set(-0.25F + brightness * 0.25F);
			b.upload();
		});
		BakerLibrary.KONDENZATOR_CORE.renderWithRotation(facing);
		ShaderLibrary.BRIGHT.end();
		GlStateManager.popMatrix();
	}
}
