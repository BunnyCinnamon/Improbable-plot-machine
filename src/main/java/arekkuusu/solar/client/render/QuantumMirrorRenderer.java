/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.client.render;

import arekkuusu.solar.api.capability.inventory.EntangledIItemHandler;
import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.SpriteLibrary;
import arekkuusu.solar.client.util.helper.GLHelper;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TileQuantumMirror;
import net.katsstuff.teamnightclipse.mirror.client.helper.Blending;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Optional;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class QuantumMirrorRenderer extends SpecialModelRenderer<TileQuantumMirror> {

	@Override
	void renderTile(TileQuantumMirror mirror, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		switch(MinecraftForgeClient.getRenderPass()) {
			case 0:
				Optional<UUID> optional = mirror.getKey();
				if(optional.isPresent()) {
					ItemStack stack = EntangledIItemHandler.getEntanglementStack(optional.get(), 0);
					if(!stack.isEmpty()) {
						renderItem(stack, tick, x, y, z, partialTicks);
					}
				}
				break;
			case 1:
				renderModel(tick, x, y, z, partialTicks);
				break;
		}
	}

	@Override
	public void renderStack(double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		ItemStack stack = SpecialModelRenderer.getTempItemRenderer();
		if(!stack.isEmpty()) {
			renderItem(stack, tick, x, y, z, partialTicks);
		}
		GLHelper.disableDepth();
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			float brigthness = MathHelper.cos(Minecraft.getMinecraft().player.ticksExisted * 0.05F);
			if(brigthness > 0) brigthness *= -1;
			b.set(brigthness);
			b.upload();
		});
		//
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate(x, y, z + 0.5F);
		SpriteLibrary.QUANTUM_MIRROR.bindManager();
		renderMirror(tick, 0.75F, 0.5F, partialTicks);
		renderMirror(-tick, 0.5F, 0.75F, partialTicks);
		renderMirror(tick, -0.3F, 1F, partialTicks);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
		//
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GLHelper.enableDepth();
	}

	private void renderItem(ItemStack stack, float tick, double x, double y, double z, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.38, z + 0.5);
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		RenderHelper.renderItemStack(stack);
		GlStateManager.popMatrix();
	}

	private void renderModel(float tick, double x, double y, double z, float partialTicks) {
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			float brigthness = MathHelper.cos(Minecraft.getMinecraft().player.ticksExisted * 0.05F);
			if(brigthness > 0) brigthness *= -1;
			b.set(brigthness);
			b.upload();
		});
		//
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
		//
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
	}

	private void renderMirror(float tick, float offset, float scale, float partialTicks) {
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
