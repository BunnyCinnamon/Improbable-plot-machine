package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.ShaderLibrary;
import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.client.util.helper.RenderHelper;
import arekkuusu.solar.common.block.tile.TilePholarizer;
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
		BlockBaker.render(BlockBaker.PHOLARIZER_BASE);
		//Piece
		GlStateManager.disableLighting();
		ShaderLibrary.BRIGHT.begin();
		ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> b.set(1F));
		BlockBaker.render(active ? BlockBaker.PHOLARIZER_POSITIVE : BlockBaker.PHOLARIZER_NEGATIVE);
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		BlockBaker.render(BlockBaker.PHOLARIZER_RING);
		GlStateManager.popMatrix();
		//Pillars
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.05F % 360F, 0F, -1F, 0F);
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick * 1.5F, 0.045F, 0.75F, partialTicks);
		BlockBaker.render(BlockBaker.PHOLARIZER_PILLAR_0);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick * 0.75F, 0.025F, 1F, partialTicks);
		BlockBaker.render(BlockBaker.PHOLARIZER_PILLAR_1);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick * 0.5F, 0.05F, 0.5F, partialTicks);
		BlockBaker.render(BlockBaker.PHOLARIZER_PILLAR_2);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		RenderHelper.makeUpDownTranslation(tick * 0.25F, 0.05F, 1.5F, partialTicks);
		BlockBaker.render(BlockBaker.PHOLARIZER_PILLAR_3);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
