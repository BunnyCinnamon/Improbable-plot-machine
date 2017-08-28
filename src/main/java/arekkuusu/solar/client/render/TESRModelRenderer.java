/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.util.baker.IModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
public abstract class TESRModelRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T> implements IModelRenderer {

	private static ItemStack tempItemStack; //Have a problem?? Come with me, I'll fucking wreck you punk

	@Nullable
	public static ItemStack getTempItemRenderer() {
		return tempItemStack;
	}

	public static void setTempItemRenderer(@Nullable ItemStack tempItemStack) {
		TESRModelRenderer.tempItemStack = tempItemStack;
	}

	@Override
	public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(te == null) {
			renderModel(x, y, z, partialTicks);
		} else {
			renderTile(te, x, y, z, partialTicks, destroyStage, alpha);
		}
	}

	abstract void renderTile(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);
}
