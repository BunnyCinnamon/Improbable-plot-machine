package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileElectronRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemElectronRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TileElectronRenderer.renderModel(true, 0, 0, 0, partialTicks);
	}
}
