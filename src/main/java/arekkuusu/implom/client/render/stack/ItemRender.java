package arekkuusu.implom.client.render.stack;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public abstract class ItemRender extends TileEntityItemStackRenderer {
	protected void bindTexture(ResourceLocation location) {
		TileEntityRendererDispatcher.instance.renderEngine.bindTexture(location);
	}
}
