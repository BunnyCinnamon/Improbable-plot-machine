package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.BakerLibrary;
import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TilePholarizer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class TilePholarizerRenderer extends TileEntitySpecialRenderer<TilePholarizer> {

	@Override
	public void render(TilePholarizer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderModel(te.getFacingLazy(), x, y, z, partialTicks);
	}

	public static void renderModel(EnumFacing facing, double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		//Base
		BakerLibrary.PHOLARIZER_FRAME.renderWithRotation(facing);
		BakerLibrary.PHOLARIZER_CORE.renderWithYOffset(facing, RenderHelper.getInterpolated(tick, 0.025F, 1.25F));
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
		//Pillar
		BakerLibrary.PHOLARIZER_CRYSTAL.renderWithRotation(facing, EnumFacing.Axis.Y, tick * -0.5F % 360F);
		ShaderLibrary.BRIGHT.end();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
