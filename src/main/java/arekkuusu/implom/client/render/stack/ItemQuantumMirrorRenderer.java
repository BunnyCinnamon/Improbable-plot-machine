package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.api.capability.InventoryHelper;
import arekkuusu.implom.client.render.tile.TileQuantumMirrorRenderer;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.helper.GLHelper;
import arekkuusu.implom.client.util.helper.RenderHelper;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ItemQuantumMirrorRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		InventoryHelper.getCapability(stack).ifPresent(instance -> {
			ItemStack mirrored = instance.getStackInSlot(0);
			if(!mirrored.isEmpty()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.5, 0.38, 0.5);
				GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
				RenderHelper.renderItemStack(mirrored);
				GlStateManager.popMatrix();
			}
		});
		GLHelper.disableDepth();
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			float brigthness = MathHelper.cos(RenderHelper.getRenderPlayerTime() * 0.05F);
			if(brigthness > 0) brigthness *= -0.8;
			b.set(0.1F + brigthness);
			b.upload();
		});
		GlStateManager.pushMatrix();
		Blending.AdditiveAlpha().apply();
		GlStateManager.disableCull();
		GlStateManager.translate(0, 0, 0.5F);
		TileQuantumMirrorRenderer.renderMirror(tick, 0.75F, 0.5F);
		TileQuantumMirrorRenderer.renderMirror(-tick, 0.5F, 0.75F);
		TileQuantumMirrorRenderer.renderMirror(tick, -0.3F, 1F);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GLHelper.enableDepth();
	}
}
