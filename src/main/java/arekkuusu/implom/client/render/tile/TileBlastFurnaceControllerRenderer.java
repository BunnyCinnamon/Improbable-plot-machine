/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.render.tile;

import arekkuusu.implom.client.util.helper.RenderHelper;
import arekkuusu.implom.common.block.tile.TileBlastFurnaceController;
import arekkuusu.implom.common.handler.data.capability.FluidMultipleTank;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/*
 * Created by <Arekkuusu> on 01/05/2020.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public class TileBlastFurnaceControllerRenderer extends TileEntitySpecialRenderer<TileBlastFurnaceController> {

	@Override
	public void render(TileBlastFurnaceController tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(!tile.active) return;
		if(tile.structureMinPos == null || tile.structureMaxPos == null) return;
		if(tile.structureMinPos == BlockPos.ORIGIN || tile.structureMaxPos == BlockPos.ORIGIN) return;

		BlockPos tilePos = tile.getPos();
		BlockPos minStructurePos = tile.structureMinPos.add(1, 1, 1);
		BlockPos maxStructurePos = tile.structureMaxPos.add(-1, 0, -1);

		FluidMultipleTank tank = tile.fluidMultipleTank;
		List<FluidStack> fluids = tank.liquids;

		if(!fluids.isEmpty()) {
			double realMinPosX = minStructurePos.getX() - tilePos.getX();
			double realMinPosZ = minStructurePos.getZ() - tilePos.getZ();

			double realMaxPosX = maxStructurePos.getX() - tilePos.getX();
			double realMaxPosZ = maxStructurePos.getZ() - tilePos.getZ();

			double basePosY = minStructurePos.getY() - tilePos.getY();

			BlockPos minPos = new BlockPos(realMinPosX, basePosY, realMinPosZ);
			BlockPos maxPos = new BlockPos(realMaxPosX, basePosY, realMaxPosZ);

			int totalHeight = 1 + Math.max(0, maxStructurePos.getY() - minStructurePos.getY());
			int[] heights = calcLiquidHeights(fluids, tank.getCapacity(), totalHeight * 1000 - (int) (RenderHelper.FLUID_OFFSET * 2000D), 100);
			double offset = RenderHelper.FLUID_OFFSET;
			for(int i = 0; i < fluids.size(); i++) {
				double height = (double) heights[i] / 1000D;
				RenderHelper.renderFluidCuboid(fluids.get(i), x, y, z, minStructurePos, minPos, maxPos, offset, offset + height, 0.0625F);
				offset += height;
			}
		}
	}

	public static int[] calcLiquidHeights(List<FluidStack> liquids, int capacity, int height, int min) {
		int[] fluidHeights = new int[liquids.size()];

		int totalFluidAmount = 0;

		if(liquids.size() > 0) {

			for(int i = 0; i < liquids.size(); i++) {
				FluidStack liquid = liquids.get(i);
				totalFluidAmount += liquid.amount;

				float h = (float) liquid.amount / (float) capacity;
				fluidHeights[i] = Math.max(min, (int) Math.ceil(h * (float) height));
			}

			if(totalFluidAmount < capacity) {
				height -= min;
			}

			int sum;
			do {
				sum = 0;
				int biggest = -1;
				int m = 0;
				for(int i = 0; i < fluidHeights.length; i++) {
					sum += fluidHeights[i];
					if(fluidHeights[i] > biggest) {
						biggest = fluidHeights[i];
						m = i;
					}
				}

				if(fluidHeights[m] == 0) {
					break;
				}

				if(sum > height) {
					fluidHeights[m]--;
				}
			} while(sum > height);
		}

		return fluidHeights;
	}
}
