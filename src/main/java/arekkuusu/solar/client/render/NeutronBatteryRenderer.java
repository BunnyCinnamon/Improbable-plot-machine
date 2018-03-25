/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileNeutronBattery;
import arekkuusu.solar.common.block.tile.TileNeutronBattery.Capacity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

/**
 * Created by <Arekkuusu> on 21/03/2018.
 * It's distributed as part of Solar.
 */
public class NeutronBatteryRenderer extends SpecialModelRenderer<TileNeutronBattery> {

	@Override
	void renderTile(TileNeutronBattery te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getCapacityLazy(), x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		ItemStack stack = SpecialModelRenderer.getTempItemRenderer();
		renderModel(NBTHelper.getEnum(Capacity.class, stack, "capacity").orElse(Capacity.BLUE), x, y, z, partialTicks);
	}

	private void renderModel(Capacity capacity, double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		BlockBaker.render(BlockBaker.NEUTRON_BATTERY_BASE);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> b.set(1F));
		RenderHelper.makeUpDownTranslation(RenderHelper.getRenderWorldTime(partialTicks), 0.025F, 0.5F, 15F);
		switch(capacity) {
			case BLUE:
				BlockBaker.render(BlockBaker.NEUTRON_BATTERY_BLUE);
				break;
			case GREEN:
				BlockBaker.render(BlockBaker.NEUTRON_BATTERY_GREEN);
				break;
			case PINK:
				BlockBaker.render(BlockBaker.NEUTRON_BATTERY_PINK);
				break;
		}
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
