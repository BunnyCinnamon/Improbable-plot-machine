/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.BakerLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileHyperConductor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by <Arekkuusu> on 17/10/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class TileHyperConductorRenderer extends TileEntitySpecialRenderer<TileHyperConductor> {

	private static final float OFFSETS[][] = {
			{0.0025F, 0.0025F, -0.0025F},
			{-0.0025F, -0.0025F, 0.0025F},
			{0.0025F, -0.0025F, 0.0025F},
			{0.0025F, 0.0025F, -0.0025F},
			{-0.0025F, -0.0025F, 0.0025F}
	};

	@Override
	public void render(TileHyperConductor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(x, y, z, partialTicks);
	}

	public static void renderModel(double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		for(int i = 0; i < 5; i++) {
			GlStateManager.pushMatrix();
			float offset[] = OFFSETS[i];
			wobble(tick, 1.5F, partialTicks, offset);
			BakerLibrary.HYPER_CONDUCTOR[i].render();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	public static void wobble(float tick, float speed, float angle, float offset[]) {
		float toDegrees = (float) Math.PI / 180F;
		angle += speed * tick;
		if(angle > 360) angle -= 360;
		double i = Math.sin(angle * toDegrees);
		GlStateManager.translate(i * offset[0], i * offset[1], i * offset[2]);
	}
}
