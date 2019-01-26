package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileFissionInducerRenderer;
import arekkuusu.implom.client.util.helper.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemFissionInducerRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		TileFissionInducerRenderer.renderModel(null, tick, 0, 0, 0, partialTicks);
	}
}
