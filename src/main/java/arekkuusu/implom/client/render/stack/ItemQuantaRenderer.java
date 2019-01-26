package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.render.tile.TileQuantaRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ItemQuantaRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		TileQuantaRenderer.renderModel(Minecraft.getMinecraft().player.ticksExisted, 0, 0, 0);
	}
}
