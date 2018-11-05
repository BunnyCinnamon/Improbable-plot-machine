package arekkuusu.implom.client.util.baker.model;

import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.baked.BakedQelaion;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

public class ModelQelaion implements IModel {

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableList.of(
				ResourceLibrary.QELAION_BASE,
				ResourceLibrary.QELAION_ON,
				ResourceLibrary.QELAION_OFF
		);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		TextureAtlasSprite base = bakedTextureGetter.apply(ResourceLibrary.QELAION_BASE);
		TextureAtlasSprite off = bakedTextureGetter.apply(ResourceLibrary.QELAION_OFF);
		TextureAtlasSprite on = bakedTextureGetter.apply(ResourceLibrary.QELAION_ON);
		return new BakedQelaion(format, base, off, on, base);
	}
}
