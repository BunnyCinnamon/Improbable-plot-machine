/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render;

import arekkuusu.implom.client.util.ShaderLibrary;
import arekkuusu.implom.client.util.baker.BlockBaker;
import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileMechanicalTranslocator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/*
 * Created by <Arekkuusu> on 21/01/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class MechanicalTranslocatorRenderer extends SpecialModelRenderer<TileMechanicalTranslocator> {

	@Override
	void renderTile(TileMechanicalTranslocator translocator, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		boolean active = translocator.isTransferable();
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		if(!active) {
			if(translocator.temp == -1) translocator.temp = RenderHelper.getRenderWorldTime(partialTicks);
		} else if(translocator.temp != -1) translocator.temp = -1;
		renderModel(translocator.getFacingLazy(), active ? tick : translocator.temp, active, x, y, z, active ? partialTicks : 0);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		float tick = RenderHelper.getRenderWorldTime(partialTicks);
		renderModel(null, tick, true, x, y, z, partialTicks);
	}

	private void renderModel(@Nullable EnumFacing facing, float tick, boolean active, double x, double y, double z, float partialTicks) {
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
		//Top & Bottom
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, 1F, 0F);
		BlockBaker.TRANSLOCATOR_BASE.render();
		GlStateManager.popMatrix();
		if(active) {
			GlStateManager.disableLighting();
			ShaderLibrary.BRIGHT.begin();
			ShaderLibrary.BRIGHT.getUniformJ("brightness").ifPresent(b -> {
				b.set(0F);
				b.upload();
			});
		}
		//Middle
		GlStateManager.pushMatrix();
		GlStateManager.rotate(partialTicks + tick * 0.5F % 360F, 0F, -1F, 0F);
		BlockBaker.TRANSLOCATOR_CENTER.render();
		GlStateManager.popMatrix();
		//Piece
		GlStateManager.pushMatrix();
		GlStateManager.translate(0, 0.25F, 0F);
		RenderHelper.makeUpDownTranslation(tick, 0.05F, 1F, 0F);
		/*--- Outwards ---*/
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.425F, 0.425F, 0.425F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 360F, 0F, 1F, 0F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 720F, 1F, 0F, 0F);
		GlStateManager.rotate(partialTicks + tick * 0.75F % 360F, 0F, 0F, 1F);
		BlockBaker.TRANSLOCATOR_PIECE_RING.render();
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		GlStateManager.rotate(partialTicks + tick % 360F, 0F, -1F, 0F);
		GlStateManager.rotate(partialTicks + tick % 720F, -1F, 0F, 0F);
		GlStateManager.rotate(partialTicks + tick % 360F, 0F, 0F, -1F);
		BlockBaker.TRANSLOCATOR_PIECE_RING.render();
		GlStateManager.popMatrix();
		/*--- Inwards ---*/
		GlStateManager.popMatrix();
		if(active) {
			GlStateManager.enableLighting();
			ShaderLibrary.BRIGHT.end();
		}
		GlStateManager.popMatrix();
	}
}
