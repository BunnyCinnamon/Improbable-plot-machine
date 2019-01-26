package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemSymmetricNegatorRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5D, 0.5D, 0.5D);
		BlockBaker.SYMMETRIC_NEGATOR_BASE.render();
		//Rings
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(partialTicks + tick, 0.01F, 1.5F, 0F);
		BlockBaker.SYMMETRIC_NEGATOR_RING.render();
		GlStateManager.popMatrix();
		//Inner core
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BlockBaker.SYMMETRIC_NEGATOR_CORE.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
