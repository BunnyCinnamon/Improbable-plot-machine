package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileAsymmetricalMachinationRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ItemAsymmetricalMachinationRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TileAsymmetricalMachinationRenderer.renderModel(EnumFacing.DOWN, 0, 0, 0, partialTicks);
	}
}
