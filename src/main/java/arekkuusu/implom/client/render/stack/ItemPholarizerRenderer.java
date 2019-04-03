package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TilePholarizerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ItemPholarizerRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TilePholarizerRenderer.renderModel(EnumFacing.DOWN, 0, 0, 0, partialTicks);
	}
}
