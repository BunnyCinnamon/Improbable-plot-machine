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
import arekkuusu.implom.common.block.BlockNeutronBattery.BatteryCapacitor;
import arekkuusu.implom.common.block.tile.TileNeutronBattery;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

/*
 * Created by <Arekkuusu> on 21/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class TileNeutronBatteryRenderer extends TileEntitySpecialRenderer<TileNeutronBattery> {

	@Override
	public void render(TileNeutronBattery te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.wrapper.inventoryInstance.getCapacitor(), te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(BatteryCapacitor capacity, EnumFacing facing, double x, double y, double z, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		if(facing != EnumFacing.UP) {
			GlStateManager.translate(0.5, 0.5, 0.5);
			switch (facing) {
				case DOWN:
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					break;
				case NORTH:
					GlStateManager.rotate(90F, -1F, 0F, 0F);
					break;
				case SOUTH:
					GlStateManager.rotate(90F, 1F, 0F, 0F);
					break;
				case WEST:
					GlStateManager.rotate(90F, 0F, 0F, 1F);
					break;
				case EAST:
					GlStateManager.rotate(90F, 0F, 0F, -1F);
					break;
			}
			GlStateManager.translate(-0.5, -0.5, -0.5);
		}
		BlockBaker.NEUTRON_BATTERY_BASE.render();
		if(capacity != null) {
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			ShaderLibrary.RECOLOR.begin();
			ShaderLibrary.RECOLOR.getUniformJ("greybase").ifPresent(greybase -> {
				greybase.set(175F / 256F);
				greybase.upload();
			});
			ShaderLibrary.RECOLOR.getUniformJ("rgba").ifPresent(rgba -> {
				float r = (capacity.getColor() >>> 16 & 0xFF) / 256F;
				float g = (capacity.getColor() >>> 8 & 0xFF) / 256F;
				float b = (capacity.getColor() & 0xFF) / 256F;
				rgba.set(r, g, b);
				rgba.upload();
			});
			RenderHelper.makeUpDownTranslation(RenderHelper.getRenderWorldTime(partialTicks), 0.025F, 1.5F, 15F);
			BlockBaker.NEUTRON_BATTERY.render();
			ShaderLibrary.RECOLOR.end();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
}
