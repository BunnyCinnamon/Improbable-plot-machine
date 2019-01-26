/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.api.helper.InventoryHelper;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.SpriteLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileQuantumMirror;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Tuple;
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
				InventoryHelper.getCapability(mirror).map(instance -> instance.getStackInSlot(0)).ifPresent(stack -> {
					if(!stack.isEmpty()) {
						GlStateManager.enableBlend();
						GlStateManager.pushMatrix();
						GlStateManager.translate(x + 0.5, y + 0.38, z + 0.5);
						GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
						RenderHelper.renderItemStack(stack);
						GlStateManager.popMatrix();
						GlStateManager.disableBlend();
					}
				});
				break;
			case 1:
				GlStateManager.disableLighting();
				ShaderLibrary.BRIGHT.begin();
				ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
					float brigthness = MathHelper.cos(RenderHelper.getRenderPlayerTime() * 0.05F);
					if(brigthness > 0) brigthness *= -0.8;
					b.set(0.1F + brigthness);
					b.upload();
				});
				GlStateManager.pushMatrix();
				GlStateManager.disableCull();
				GlStateManager.enableBlend();
				Blending.AdditiveAlpha().apply();
				GlStateManager.translate(x, y, z + 0.5F);
				SpriteLibrary.QUANTUM_MIRROR.bindManager();
				renderMirror(tick, 0.75F, 0.5F, partialTicks);
				renderMirror(-tick, 0.5F, 0.75F, partialTicks);
				renderMirror(tick, -0.3F, 1F, partialTicks);
				GlStateManager.disableBlend();
				GlStateManager.enableCull();
				GlStateManager.popMatrix();
				ShaderLibrary.BRIGHT.end();
				GlStateManager.enableLighting();
				break;
		}
	}

	public static void renderMirror(float tick, float offset, float scale, float partialTicks) {
		GlStateManager.pushMatrix();
		Tuple<Double, Double> uv = SpriteLibrary.QUANTUM_MIRROR.getUVFrame((int) (tick * 0.25F));
		double vOffset = SpriteLibrary.QUANTUM_MIRROR.getV();
		double v = uv.getSecond();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buff = tessellator.getBuffer();
		GlStateManager.translate(0.5F, 0.5F, 0F);
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(partialTicks + tick * offset % 360F, 0F, 1F, 0F);
		GlStateManager.rotate(partialTicks + tick * offset % 360F, 1F, 0F, 0F);
		GlStateManager.rotate(partialTicks + tick * offset % 360F, 0F, 0F, 1F);
		GlStateManager.translate(-0.5F, -0.5F, 0F);
		buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buff.pos(0, 0, 0).tex(0, v).endVertex();
		buff.pos(1, 0, 0).tex(1, v).endVertex();
		buff.pos(1, 1, 0).tex(1, v + vOffset).endVertex();
		buff.pos(0, 1, 0).tex(0, v + vOffset).endVertex();
		tessellator.draw();
		GlStateManager.popMatrix();
	}
}
