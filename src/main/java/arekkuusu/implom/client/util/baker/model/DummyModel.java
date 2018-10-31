/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.baker.model;

import arekkuusu.implom.client.util.baker.DummyBakedRegistry;
import com.google.common.collect.ImmutableList;
import net.katsstuff.teamnightclipse.mirror.client.baked.Baked;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/*
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class DummyModel implements IModel {

	private final ResourceLocation location;
	private Baked baked;

	public DummyModel(ResourceLocation location) {
		this.location = location;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of(location);
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return baked != null ? ImmutableList.copyOf(baked.getTextures()) : Collections.emptyList();
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		return baked = DummyBakedRegistry.getBaked(location, format, getter);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}
}
