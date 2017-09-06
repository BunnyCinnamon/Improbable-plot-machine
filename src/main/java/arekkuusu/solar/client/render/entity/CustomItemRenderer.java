/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 08/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class CustomItemRenderer extends Render<EntityItem> {

	private final RenderItem render;
	private final boolean renderText;

	public CustomItemRenderer(RenderManager manager, boolean renderText) {
		super(manager);
		this.render = Minecraft.getMinecraft().getRenderItem();
		this.renderText = renderText;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void doRender(EntityItem item, double x, double y, double z, float entityYaw, float partialTicks) {
		ItemStack stack = item.getItem();

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		RenderHelper.enableStandardItemLighting();

		if(renderText) {
			renderLivingLabel(item, String.valueOf(stack.getCount()), x, y, z, 64);
		}

		boolean texture = false;

		if(bindEntityTexture(item)) {
			renderManager.renderEngine.getTexture(getEntityTexture(item)).setBlurMipmap(false, false);
			texture = true;
		}

		if(this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(item));
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(item.ticksExisted % 360, 0, 1, 0);
		GlStateManager.rotate(30.0f * (float) Math.sin(Math.toRadians(partialTicks / 3.0f + item.ticksExisted / 3 % 360)), 1, 0, 0);

		render.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);

		GlStateManager.popMatrix();

		if(this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		if(texture) {
			renderManager.renderEngine.getTexture(getEntityTexture(item)).restoreLastBlurMipmap();
		}
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

		super.doRender(item, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityItem entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
