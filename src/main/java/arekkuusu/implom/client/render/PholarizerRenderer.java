package arekkuusu.implom.client.render;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TilePholarizer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class PholarizerRenderer extends SpecialModelRenderer<TilePholarizer> {

	@Override
	void renderTile(TilePholarizer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(te.getFacingLazy(), te.getPolarizationLazy().isPositive(), x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(null, true, x, y, z, partialTicks);
	}

	private void renderModel(@Nullable EnumFacing facing, boolean active, double x, double y, double z, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		if(facing != null && facing != EnumFacing.DOWN) {
			switch(facing) {
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
			b.set(0F);
			b.upload();
		});
		if(active) BlockBaker.PHOLARIZER_POSITIVE.render();
		else BlockBaker.PHOLARIZER_NEGATIVE.render();
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		//Pillars
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.05F % 360F, 0F, -1F, 0F);
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick * 1.5F, 0.045F, 0.75F, partialTicks);
		BlockBaker.PHOLARIZER_PILLAR.render();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
