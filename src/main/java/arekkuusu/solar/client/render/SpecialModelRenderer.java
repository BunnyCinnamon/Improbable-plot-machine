/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 28/07/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public abstract class SpecialModelRenderer<T extends TileEntity> extends TileEntitySpecialRenderer<T> {

	private static ItemStack tempItemStack = ItemStack.EMPTY;

	public static ItemStack getTempItemRenderer() {
		ItemStack stack = SpecialModelRenderer.tempItemStack;
		SpecialModelRenderer.tempItemStack = ItemStack.EMPTY;
		return stack;
	}

	public static void setTempItemRenderer(@Nullable ItemStack tempItemStack) {
		SpecialModelRenderer.tempItemStack = tempItemStack;
	}

	@Override
	public void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(te == null) {
			renderStack(x, y, z, partialTicks);
		} else if(te.getWorld().isBlockLoaded(te.getPos(), false)) {
			renderTile(te, x, y, z, partialTicks, destroyStage, alpha);
		}
	}

	abstract void renderTile(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha);

	abstract void renderStack(double x, double y, double z, float partialTicks);
}
