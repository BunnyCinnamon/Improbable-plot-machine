/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ModelBakery;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.BlendHelper;
import arekkuusu.solar.common.block.tile.TilePrismFlower;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 14/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class TilePrismFlowerRenderer extends TileEntitySpecialRenderer<TilePrismFlower> {

	@Override
	public void render(TilePrismFlower flower, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!flower.getWorld().isBlockLoaded(flower.getPos(), false)) return;

		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();

		Vec3d offset = flower.getOffSet(x, y, z);
		GlStateManager.translate(offset.x, offset.y, offset.z);

		GlStateManager.rotate((flower.tick * 0.25F) % 360, 1, 1, 1);

		GlStateManager.scale(0.75F, 0.75F, 0.75F);

		float brightness = 255F * flower.brightness;
		BlendHelper.lightMap(brightness, brightness);

		SpriteLibrary.PRISM_PETAL.bindManager();
		double min = 0.3125D;
		double max = 0.6875D;
		ModelBakery.renderCube(0.15F, min, max, min, max);

		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}
}
