package cinnamon.implom.client.render.tile;

import cinnamon.implom.client.util.helper.FluidRenderer;
import cinnamon.implom.common.block.tile.TileBlastFurnaceController;
import cinnamon.implom.common.handler.data.capability.MultipleTank;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class TileBlastFurnaceControllerRenderer implements BlockEntityRenderer<TileBlastFurnaceController> {

    public TileBlastFurnaceControllerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TileBlastFurnaceController tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if(!tile.active) return;
        if(tile.structureMinPos == null || tile.structureMaxPos == null) return;
        if(tile.structureMinPos == BlockPos.ZERO || tile.structureMaxPos == BlockPos.ZERO) return;

        BlockPos tilePos = tile.getBlockPos();
        BlockPos minStructurePos = tile.structureMinPos.offset(1, 1, 1);
        BlockPos maxStructurePos = tile.structureMaxPos.offset(-1, 0, -1);

        MultipleTank tank = tile.meltingTank;
        List<FluidStack> fluids = tank.fluids;

        if(!fluids.isEmpty()) {
            double realMinPosX = minStructurePos.getX() - tilePos.getX();
            double realMinPosZ = minStructurePos.getZ() - tilePos.getZ();

            double realMaxPosX = maxStructurePos.getX() - tilePos.getX();
            double realMaxPosZ = maxStructurePos.getZ() - tilePos.getZ();

            double basePosY = minStructurePos.getY() - tilePos.getY();

            BlockPos minPos = new BlockPos(realMinPosX, basePosY, realMinPosZ);
            BlockPos maxPos = new BlockPos(realMaxPosX, basePosY, realMaxPosZ);

            int totalHeight = 1 + Math.max(0, maxStructurePos.getY() - minStructurePos.getY());
            int[] heights = calcLiquidHeights(fluids, tank.maxCapacity, totalHeight * 1000 - (int) (FluidRenderer.FLUID_OFFSET * 2000D), 100);
            float offset = FluidRenderer.FLUID_OFFSET;
            for(int i = 0; i < fluids.size(); i++) {
                float height = (float) heights[i] / 1000F;
                FluidRenderer.renderFluidCuboid(poseStack, buffer, fluids.get(i), minPos, maxPos, combinedLightIn, offset, offset + height);
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
                totalFluidAmount += liquid.getAmount();

                float h = (float) liquid.getAmount() / (float) capacity;
                fluidHeights[i] = Math.max(min, (int) Math.ceil(h * (float) height));
            }

            if(totalFluidAmount < capacity) {
                height -= min;
            }

            int sum;
            do {
                sum = 0;
                int biggest = -1;
                int index = 0;
                for(int i = 0; i < fluidHeights.length; i++) {
                    sum += fluidHeights[i];
                    if(fluidHeights[i] > biggest) {
                        biggest = fluidHeights[i];
                        index = i;
                    }
                }

                if(fluidHeights[index] == 0) {
                    break;
                }

                if(sum > height) {
                    fluidHeights[index]--;
                }
            } while(sum > height);
        }

        return fluidHeights;
    }
}
