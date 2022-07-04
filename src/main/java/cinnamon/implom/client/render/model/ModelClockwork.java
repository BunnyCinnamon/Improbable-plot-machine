package cinnamon.implom.client.render.model;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.DynamicBucketModel;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class ModelClockwork implements IModelGeometry<ModelClockwork> {

    public static Transformation mkTransform(float tx, float ty, float tz, float ax, float ay, float az, float size) {
        return new Transformation(
                new Vector3f(tx / 16, ty / 16, tz / 16),
                new Quaternion(ax, ay, az, 0F),
                new Vector3f(size, size, size),
                null
        );
    }

    public static final ImmutableMap<ItemTransforms.TransformType, Transformation> ITEM_TRANSFORMS = ImmutableMap.<ItemTransforms.TransformType, Transformation>builder()
            .put(ItemTransforms.TransformType.GUI, mkTransform(0F, 0F, 0F, 0F, 0F, 0F, 1F))
            .put(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, mkTransform(3.6F, 6.6F, 4.6F, 0F, 0F, 0F, 0.55F))
            .put(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, mkTransform(3.6F, 6.6F, 4.6F, 0F, 0F, 0F, 0.55F))
            .put(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, mkTransform(14.57F, 4F, 6.5F, 0F, -90F, 25F, 0.68F))
            .put(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, mkTransform(14.57F, 4F, 6.5F, 0F, -90F, 25F, 0.68F))
            .put(ItemTransforms.TransformType.GROUND, mkTransform(4F, 6F, 4F, 0F, 0F, 0F, 0.5F))
            .put(ItemTransforms.TransformType.FIXED, mkTransform(0F, 0F, 0F, 0F, 0F, 0F, 1F))
            .put(ItemTransforms.TransformType.HEAD, mkTransform(0F, 13F, 7F, 0F, 0F, 0F, 1F))
            .build();

    public ModelClockwork() {
        new DynamicBucketModel(null, false, false, false, false);
    }

    @Override
    public BakedModel bake(IModelConfiguration iModelConfiguration, ModelBakery arg, Function<Material, TextureAtlasSprite> function, ModelState arg2, net.minecraft.client.renderer.block.model.ItemOverrides arg3, ResourceLocation arg4) {
        return null;
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration iModelConfiguration, Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {
        return null;
    }

    private static class ItemOverrides extends net.minecraft.client.renderer.block.model.ItemOverrides {

        @org.jetbrains.annotations.Nullable
        @Override
        public BakedModel resolve(BakedModel arg, ItemStack arg2, @org.jetbrains.annotations.Nullable ClientLevel arg3, @org.jetbrains.annotations.Nullable LivingEntity arg4, int k) {
            return super.resolve(arg, arg2, arg3, arg4, k);
        }
    }
}
