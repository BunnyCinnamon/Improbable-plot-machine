package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.client.util.BakerLibrary;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ItemSymmetricNegatorRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		BakerLibrary.SYMMETRIC_SENDER_FRAME.render();
		//Rings
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick, 0.01F, 1.5F);
		BakerLibrary.SYMMETRIC_SENDER_RING.render();
		GlStateManager.popMatrix();
		//Inner core
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			b.set(0F);
			b.upload();
		});
		BakerLibrary.SYMMETRIC_SENDER_CORE.render();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
