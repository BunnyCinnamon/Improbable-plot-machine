package arekkuusu.implom.client.util.baker.model;

import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.baked.BakedRendered;
import com.google.common.collect.ImmutableList;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedPerspective;
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
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModelRendered implements IModel {

	private Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
	private List<ResourceLocation> dependencies;
	private ItemOverrideList override;
	private ResourceLocation particle;

	public ModelRendered() {
		this(BakedPerspective.BlockTransformsJava(), ImmutableList.of(), new ItemOverrideList(ImmutableList.of()), ResourceLibrary.EMPTY);
	}

	public ModelRendered(Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, List<ResourceLocation> dependencies, ItemOverrideList override, ResourceLocation particle) {
		this.transforms = transforms;
		this.override = override;
		this.particle = particle;
		this.dependencies = dependencies;
	}

	public ModelRendered setTransforms(Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
		return new ModelRendered(transforms, this.dependencies, this.override, this.particle);
	}

	public ModelRendered setDependencies(List<ResourceLocation> dependencies) {
		return new ModelRendered(this.transforms, dependencies, this.override, this.particle);
	}

	public ModelRendered setOverride(ItemOverrideList override) {
		return new ModelRendered(this.transforms, this.dependencies, override, this.particle);
	}

	public ModelRendered setOverride(Consumer<ItemStack> consumer) {
		ItemOverrideList override = new ItemOverrideList(ImmutableList.of()) {
			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
				if(!stack.isEmpty()) {
					consumer.accept(stack.copy());
				}
				return originalModel;
			}
		};
		return setOverride(override);
	}

	public ModelRendered setParticle(ResourceLocation particle) {
		return new ModelRendered(this.transforms, this.dependencies, this.override, particle);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return dependencies;
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableList.of(particle);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new BakedRendered(transforms, override, particle);
	}
}
