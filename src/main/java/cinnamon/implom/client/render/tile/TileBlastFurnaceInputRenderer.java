package cinnamon.implom.client.render.tile;

import cinnamon.implom.common.block.tile.TileBlastFurnaceInput;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.OptionalInt;

public class TileBlastFurnaceInputRenderer implements BlockEntityRenderer<TileBlastFurnaceInput> {

    private final ItemRenderer itemRenderer;

    public TileBlastFurnaceInputRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(TileBlastFurnaceInput tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        var k = (int) tile.getBlockPos().asLong();
        var itemstack = tile.filter;
        var direction = tile.getFacingLazy();
        var lightColor = LevelRenderer.getLightColor(tile.getLevel(), tile.getBlockPos().relative(direction));
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.translate(direction.getStepX() * 0.5, direction.getStepY(), direction.getStepZ() * 0.5);
        poseStack.mulPose(new Quaternionf().rotationX(180.0f - direction.toYRot()));
        if (!itemstack.isEmpty()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
            this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, lightColor, OverlayTexture.NO_OVERLAY, poseStack, buffer, tile.getLevel(), k);
        }
        poseStack.popPose();
    }

    public OptionalInt getFramedMapId(ItemStack itemStack) {
        Integer integer;
        if (itemStack.is(Items.FILLED_MAP) && (integer = MapItem.getMapId(itemStack)) != null) {
            return OptionalInt.of(integer);
        }
        return OptionalInt.empty();
    }
}
