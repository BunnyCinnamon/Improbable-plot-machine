package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileMutatorRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemMutatorRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TileMutatorRenderer.renderModel(null, 0, 0, 0, partialTicks);
	}
}
