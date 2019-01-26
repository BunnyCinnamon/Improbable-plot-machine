package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileMechanicalTranslocatorRenderer;
import arekkuusu.implom.client.util.helper.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemMechanicalTranslocatorRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		TileMechanicalTranslocatorRenderer.renderModel(null, tick, true, 0, 0, 0, partialTicks);
	}
}
