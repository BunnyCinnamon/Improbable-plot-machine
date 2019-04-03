package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileSymmetricExtensionRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ItemSymmetricExtensionRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TileSymmetricExtensionRenderer.renderModel(EnumFacing.DOWN, 0, 0, 0, partialTicks);
	}
}
