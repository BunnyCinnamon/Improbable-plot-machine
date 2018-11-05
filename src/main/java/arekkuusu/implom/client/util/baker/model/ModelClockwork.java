package arekkuusu.implom.client.util.baker.model;

import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.baked.BakedClockwork;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class ModelClockwork implements IModel {

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
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		Optional<TRSRTransformation> transform = state.apply(Optional.empty());
		for(int i = 0; i < textures.size(); i++) {
			TextureAtlasSprite sprite = bakedTextureGetter.apply(textures.get(i));
			builder.addAll(ItemLayerModel.getQuadsForSprite(i, sprite, format, transform));
		}
		TextureAtlasSprite particle = bakedTextureGetter.apply(textures.isEmpty() ? new ResourceLocation("missingno") : textures.get(0));
		ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = PerspectiveMapWrapper.getTransforms(state);
		return new BakedClockwork(this, builder.build(), particle, map, overrides, format);
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
