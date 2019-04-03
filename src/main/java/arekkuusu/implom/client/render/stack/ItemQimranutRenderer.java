package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileQimranutRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ItemQimranutRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TileQimranutRenderer.renderModel(EnumFacing.DOWN, 0, 0, 0, partialTicks);
	}
}
