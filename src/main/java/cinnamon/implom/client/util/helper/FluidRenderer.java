package cinnamon.implom.client.util.helper;

import cinnamon.implom.IPM;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.apache.commons.lang3.NotImplementedException;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class FluidRenderer extends RenderStateShard {

    public static final RenderType RENDER_TYPE = RenderType.create(
            new ResourceLocation(IPM.MOD_ID, "smeltery_fluid").toString(), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder()
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                    .setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false));
    public static float FLUID_OFFSET = 0.005F;

    public FluidRenderer(String string, Runnable runnable, Runnable runnable2) {
        super(string, runnable, runnable2);
        throw new NotImplementedException();
    }

    public static void renderFluidCuboid(PoseStack poseStack, MultiBufferSource buffer, FluidStack fluidStack,
                                         BlockPos minPos, BlockPos maxPos,
                                         int brightness, float yMin, float yMax) {
        if (yMin >= yMax || fluidStack.isEmpty()) {
            return;
        }

        FluidType attributes = fluidStack.getFluid().getFluidType();
        TextureAtlasSprite still = FluidRenderer.getBlockSprite(IClientFluidTypeExtensions.of(attributes).getStillTexture(fluidStack));
        int color = IClientFluidTypeExtensions.of(attributes).getTintColor();
        brightness = FluidRenderer.withBlockLight(brightness, attributes.getLightLevel(fluidStack));
        boolean upsideDown = attributes.getDensity(fluidStack) <= 0;

        int yd = (int) (yMax - (int) yMin);
        int xd = maxPos.getX() - minPos.getX();
        int zd = maxPos.getZ() - minPos.getZ();
        // except in the rare case of yMax perfectly aligned with the block, causing the top face to render multiple times
        // for example, if yMax = 3 and yMin = 1, the values of the face array become 1, 2, 3, 3 as we then have middle ints
        if (yMax % 1d == 0) yd--;
        float[] yBounds = getBlockBounds(yd, yMin, yMax);
        float[] xBounds = getBlockBounds(xd);
        float[] zBounds = getBlockBounds(zd);

        VertexConsumer builder = buffer.getBuffer(FluidRenderer.RENDER_TYPE);

        // render each side
        Matrix4f matrix = poseStack.last().pose();
        Vector3f from = new Vector3f();
        Vector3f to = new Vector3f();
        int rotation = upsideDown ? 180 : 0;
        for (int y = 0; y <= yd; y++) {
            for (int z = 0; z <= zd; z++) {
                for (int x = 0; x <= xd; x++) {
                    from.set(xBounds[x]-3, yBounds[y], zBounds[z]-1);
                    to.set(xBounds[x + 1]-3, yBounds[y + 1], zBounds[z + 1]-1);
                    if (x == 0)
                        FluidRenderer.putTexturedQuad(builder, matrix, still, from, to, Direction.WEST, color, brightness, rotation, false);
                    if (x == xd)
                        FluidRenderer.putTexturedQuad(builder, matrix, still, from, to, Direction.EAST, color, brightness, rotation, false);
                    if (z == 0)
                        FluidRenderer.putTexturedQuad(builder, matrix, still, from, to, Direction.NORTH, color, brightness, rotation, false);
                    if (z == zd)
                        FluidRenderer.putTexturedQuad(builder, matrix, still, from, to, Direction.SOUTH, color, brightness, rotation, false);
                    if (y == yd)
                        FluidRenderer.putTexturedQuad(builder, matrix, still, from, to, Direction.UP, color, brightness, rotation, false);
                    if (y == 0) {
                        // increase Y position slightly to prevent z fighting on neighboring fluids
                        from.y = (from.y() + 0.001f);
                        FluidRenderer.putTexturedQuad(builder, matrix, still, from, to, Direction.DOWN, color, brightness, rotation, false);
                    }
                }
            }
        }
    }

    public static void putTexturedQuad(VertexConsumer renderer, Matrix4f matrix, TextureAtlasSprite sprite, Vector3f from, Vector3f to, Direction face, int color, int brightness, int rotation, boolean flowing) {
        float x1 = from.x(), y1 = from.y(), z1 = from.z();
        float x2 = to.x(), y2 = to.y(), z2 = to.z();
        float u1, u2, v1, v2;
        switch (face) {
            default -> {
                u1 = x1;
                u2 = x2;
                v1 = z2;
                v2 = z1;
            }
            case UP -> {
                u1 = x1;
                u2 = x2;
                v1 = -z1;
                v2 = -z2;
            }
            case NORTH -> {
                u1 = -x1;
                u2 = -x2;
                v1 = y1;
                v2 = y2;
            }
            case SOUTH -> {
                u1 = x2;
                u2 = x1;
                v1 = y1;
                v2 = y2;
            }
            case WEST -> {
                u1 = z2;
                u2 = z1;
                v1 = y1;
                v2 = y2;
            }
            case EAST -> {
                u1 = -z1;
                u2 = -z2;
                v1 = y1;
                v2 = y2;
            }
        }

        if (rotation == 0 || rotation == 270) {
            float temp = v1;
            v1 = -v2;
            v2 = -temp;
        }
        if (rotation >= 180) {
            float temp = u1;
            u1 = -u2;
            u2 = -temp;
        }

        boolean reverse = u1 > u2;
        u1 = boundUV(u1, reverse);
        u2 = boundUV(u2, !reverse);
        reverse = v1 > v2;
        v1 = boundUV(v1, reverse);
        v2 = boundUV(v2, !reverse);

        float minU, maxU, minV, maxV;
        double size = flowing ? 8 : 16;
        if ((rotation % 180) == 90) {
            minU = sprite.getU(v1 * size);
            maxU = sprite.getU(v2 * size);
            minV = sprite.getV(u1 * size);
            maxV = sprite.getV(u2 * size);
        } else {
            minU = sprite.getU(u1 * size);
            maxU = sprite.getU(u2 * size);
            minV = sprite.getV(v1 * size);
            maxV = sprite.getV(v2 * size);
        }
        float u3, u4, v3, v4;
        switch (rotation) {
            default -> {
                u1 = minU;
                v1 = maxV;
                u2 = minU;
                v2 = minV;
                u3 = maxU;
                v3 = minV;
                u4 = maxU;
                v4 = maxV;
            }
            case 90 -> {
                u1 = minU;
                v1 = minV;
                u2 = maxU;
                v2 = minV;
                u3 = maxU;
                v3 = maxV;
                u4 = minU;
                v4 = maxV;
            }
            case 180 -> {
                u1 = maxU;
                v1 = minV;
                u2 = maxU;
                v2 = maxV;
                u3 = minU;
                v3 = maxV;
                u4 = minU;
                v4 = minV;
            }
            case 270 -> {
                u1 = maxU;
                v1 = maxV;
                u2 = minU;
                v2 = maxV;
                u3 = minU;
                v3 = minV;
                u4 = maxU;
                v4 = minV;
            }
        }
        int light1 = brightness & 0xFFFF;
        int light2 = brightness >> 0x10 & 0xFFFF;
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        switch (face) {
            case DOWN -> {
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u1, v1).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u2, v2).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u3, v3).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u4, v4).uv2(light1, light2).endVertex();
            }
            case UP -> {
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u1, v1).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u2, v2).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u3, v3).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u4, v4).uv2(light1, light2).endVertex();
            }
            case NORTH -> {
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u1, v1).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u2, v2).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u3, v3).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u4, v4).uv2(light1, light2).endVertex();
            }
            case SOUTH -> {
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u1, v1).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u2, v2).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u3, v3).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u4, v4).uv2(light1, light2).endVertex();
            }
            case WEST -> {
                renderer.vertex(matrix, x1, y1, z2).color(r, g, b, a).uv(u1, v1).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y2, z2).color(r, g, b, a).uv(u2, v2).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y2, z1).color(r, g, b, a).uv(u3, v3).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x1, y1, z1).color(r, g, b, a).uv(u4, v4).uv2(light1, light2).endVertex();
            }
            case EAST -> {
                renderer.vertex(matrix, x2, y1, z1).color(r, g, b, a).uv(u1, v1).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y2, z1).color(r, g, b, a).uv(u2, v2).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y2, z2).color(r, g, b, a).uv(u3, v3).uv2(light1, light2).endVertex();
                renderer.vertex(matrix, x2, y1, z2).color(r, g, b, a).uv(u4, v4).uv2(light1, light2).endVertex();
            }
        }
    }

    private static float boundUV(float value, boolean upper) {
        value = value % 1;
        if (value == 0) {
            return upper ? 1 : 0;
        }
        return value < 0 ? (value + 1) : value;
    }

    private static float[] getBlockBounds(int delta) {
        return getBlockBounds(delta, FLUID_OFFSET, delta + 1f - FLUID_OFFSET);
    }

    private static float[] getBlockBounds(int delta, float start, float end) {
        float[] bounds = new float[2 + delta];
        bounds[0] = start;
        int offset = (int) start;
        for (int i = 1; i <= delta; i++) bounds[i] = i + offset;
        bounds[delta + 1] = end;
        return bounds;
    }

    public static TextureAtlasSprite getBlockSprite(ResourceLocation sprite) {
        return Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(sprite);
    }

    public static int withBlockLight(int combinedLight, int blockLight) {
        return (combinedLight & 0xFFFF0000) | Math.max(blockLight << 4, combinedLight & 0xFFFF);
    }
}
