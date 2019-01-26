package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TilePholarizer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class TilePholarizerRenderer extends TileEntitySpecialRenderer<TilePholarizer> {

	@Override
	public void render(TilePholarizer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(@Nullable EnumFacing facing, double x, double y, double z, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		if(facing != null && facing != EnumFacing.DOWN) {
			switch (facing) {
				case UP:
					GlStateManager.rotate(180F, 1F, 0F, 0F);
					break;
				case NORTH:
					GlStateManager.rotate(90F, 1F, 0F, 0F);
					break;
				case SOUTH:
					GlStateManager.rotate(90F, -1F, 0F, 0F);
					break;
				case WEST:
					GlStateManager.rotate(90F, 0F, 0F, -1F);
					break;
				case EAST:
					GlStateManager.rotate(90F, 0F, 0F, 1F);
					break;
			}
		}
		//Base
		BlockBaker.PHOLARIZER_BASE.render();
		//Piece
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
			float brigthness = MathHelper.cos(RenderHelper.getRenderPlayerTime() * 0.05F);
			if(brigthness > 0) brigthness *= -0.1;
			else brigthness *= 0.1;
			b.set(0F + brigthness);
			b.upload();
		});
		BlockBaker.PHOLARIZER_BOTTOM.render();
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		//Pillars
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, -1F, 0F);
		BlockBaker.PHOLARIZER_PILLAR.render();
		GlStateManager.popMatrix();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
