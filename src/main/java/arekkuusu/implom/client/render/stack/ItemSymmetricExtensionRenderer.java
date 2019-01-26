package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileSymmetricExtensionRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemSymmetricExtensionRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5D, 0.5D, 0.5D);
		TileSymmetricExtensionRenderer.renderModel(partialTicks);
		GlStateManager.popMatrix();
	}
}
