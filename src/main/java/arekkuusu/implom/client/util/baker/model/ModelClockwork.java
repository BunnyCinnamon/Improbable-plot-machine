package arekkuusu.implom.client.util.baker.model;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.baked.BakedClockwork;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedPerspective;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class ModelClockwork implements IModel {

	public static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> ITEM_TRANSFORMS = ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
			.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, 0F, 0F, 0F, 0F, 0F, 1F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(3.6F, 6.6F, 4.6F, 0F, 0F, 0F, 0.55F))
			.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(3.6F, 6.6F, 4.6F, 0F, 0F, 0F, 0.55F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(14.57F, 4F, 6.5F, 0F, -90F, 25F, 0.68F))
			.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(14.57F, 4F, 6.5F, 0F, -90F, 25F, 0.68F))
			.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(4F, 6F, 4F, 0F, 0F, 0F, 0.5F))
			.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 0F, 0F, 0F, 0F, 0F, 1F))
			.put(ItemCameraTransforms.TransformType.HEAD, BakedPerspective.mkTransform(0F, 13F, 7F, 0F, 0F, 0F, 1F))
			.build();
	private final ImmutableList<ResourceLocation> textures;
	private final ItemOverrideList overrides;

	public ModelClockwork() {
		this(ImmutableList.of(
				ResourceLibrary.CLOCKWORK_INSIDES,
				ResourceLibrary.CLOCKWORK_SEALED
		), new ItemOverrides());
	}

	public ModelClockwork(ImmutableList<ResourceLocation> textures, ItemOverrideList overrides) {
		this.textures = textures;
		this.overrides = overrides;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = ImmutableMap.copyOf(ITEM_TRANSFORMS);
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		Optional<TRSRTransformation> transform = state.apply(Optional.empty());
		for(int i = 0; i < textures.size(); i++) {
			TextureAtlasSprite sprite = bakedTextureGetter.apply(textures.get(i));
			builder.addAll(ItemLayerModel.getQuadsForSprite(i, sprite, format, transform));
		}
		TextureAtlasSprite particle = bakedTextureGetter.apply(textures.isEmpty() ? new ResourceLocation("missingno") : textures.get(0));
		return new BakedClockwork(this, builder.build(), particle, transformMap, overrides, format);
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData) {
		ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
		if(customData.containsKey("background")) {
			builder.add(new ResourceLocation(customData.get("background")));
		}
		if(customData.containsKey("quartz")) {
			builder.add(new ResourceLocation(customData.get("quartz")));
		}
		if(customData.containsKey("overlay")) {
			builder.add(new ResourceLocation(customData.get("overlay")));
		}
		return new ModelClockwork(builder.build(), overrides);
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return textures;
	}

	private static class ItemOverrides extends ItemOverrideList {

		ItemOverrides() {
			super(ImmutableList.of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
			Function<ResourceLocation, TextureAtlasSprite> textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
			boolean unsealed = NBTHelper.getBoolean(stack, "unsealed");
			ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
			builder.put("background", ResourceLibrary.CLOCKWORK_INSIDES.toString());
			NBTHelper.getNBTTag(stack, "quartz")
					.map(ItemStack::new)
					.map(quartz -> {
						RenderItem render = Minecraft.getMinecraft().getRenderItem();
						IBakedModel model = render.getItemModelWithOverrides(quartz, null, null);
						return model.getParticleTexture();
					})
					.map(TextureAtlasSprite::getIconName)
					.ifPresent(name -> builder.put("quartz", name));
			builder.put("overlay", unsealed ? ResourceLibrary.CLOCKWORK_UNSEALED.toString() : ResourceLibrary.CLOCKWORK_SEALED.toString());
			BakedClockwork baked = (BakedClockwork) originalModel;
			return baked.parent.process(builder.build()).bake(new SimpleModelState(baked.getTransforms()), baked.format, textureGetter);
		}
	}
}
