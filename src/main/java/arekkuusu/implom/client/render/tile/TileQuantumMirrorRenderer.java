/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.api.capability.InventoryHelper;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.SpriteLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.client.util.sprite.UVFrame;
import arekkuusu.implom.common.block.tile.TileQuantumMirror;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/*
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class TileQuantumMirrorRenderer extends TileEntitySpecialRenderer<TileQuantumMirror> {

	@Override
	public void render(TileQuantumMirror mirror, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		switch (MinecraftForgeClient.getRenderPass()) {
			case 0:
				InventoryHelper.getCapability(mirror).ifPresent(instance -> {
					ItemStack stack = instance.getStackInSlot(0);
					if(!stack.isEmpty()) {
						GlStateManager.pushMatrix();
						GlStateManager.enableBlend();
						GlStateManager.translate(x + 0.5, y + 0.38, z + 0.5);
						GlStateManager.rotate(tick * 0.5F % 360F, 0F, 1F, 0F);
						RenderHelper.renderItemStack(stack);
						GlStateManager.disableBlend();
						GlStateManager.popMatrix();
					}
				});
				break;
			case 1:
				GlStateManager.pushMatrix();
				GlStateManager.disableCull();
				GlStateManager.enableBlend();
				GlStateManager.disableLighting();
				Blending.AdditiveAlpha().apply();
				ShaderLibrary.BRIGHT.begin();
				ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
					float brigthness = MathHelper.cos(RenderHelper.getRenderPlayerTime() * 0.05F);
					if(brigthness > 0) brigthness *= -0.8;
					b.set(0.1F + brigthness);
					b.upload();
				});
				GlStateManager.translate(x, y, z + 0.5F);
				renderMirror(tick, 0.75F, 0.5F);
				renderMirror(-tick, 0.5F, 0.75F);
				renderMirror(tick, -0.3F, 1F);
				ShaderLibrary.BRIGHT.end();
				GlStateManager.enableLighting();
				GlStateManager.disableBlend();
				GlStateManager.enableCull();
				GlStateManager.popMatrix();
				break;
		}
	}

	public static void renderMirror(float tick, float rotationOffset, float scale) {
		GlStateManager.pushMatrix();
		SpriteLibrary.QUANTUM_MIRROR.bind();
		UVFrame frame = SpriteLibrary.QUANTUM_MIRROR.getFrame(tick * 0.25F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		GlStateManager.translate(0.5F, 0.5F, 0F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(tick * rotationOffset % 360F, 0F, 1F, 0F);
		GlStateManager.rotate(tick * rotationOffset % 360F, 1F, 0F, 0F);
		GlStateManager.rotate(tick * rotationOffset % 360F, 0F, 0F, 1F);
		GlStateManager.translate(-0.5F, -0.5F, 0F);
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buff.pos(0, 0, 0).tex(frame.uMin, frame.vMax).endVertex();
		buff.pos(1, 0, 0).tex(frame.uMax, frame.vMax).endVertex();
		buff.pos(1, 1, 0).tex(frame.uMax, frame.vMin).endVertex();
		buff.pos(0, 1, 0).tex(frame.uMin, frame.vMin).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
	}
}
