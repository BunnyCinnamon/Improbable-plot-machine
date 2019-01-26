package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileVacuumConveyorRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemVacuumConveyorRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TileVacuumConveyorRenderer.renderModel(null, 0, 0, 0, partialTicks);
	}
}
