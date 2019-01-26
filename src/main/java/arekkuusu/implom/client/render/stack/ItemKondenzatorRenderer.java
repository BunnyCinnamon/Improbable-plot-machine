package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileKondenzatorRenderer;
import arekkuusu.implom.common.block.BlockKondenzator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemKondenzatorRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TileKondenzatorRenderer.renderModel(BlockKondenzator.Constants.LUMEN_CAPACITY, null, 0, 0, 0);
	}
}
