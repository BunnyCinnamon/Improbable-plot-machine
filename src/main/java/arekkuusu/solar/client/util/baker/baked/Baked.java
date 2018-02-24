/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import com.google.common.collect.ImmutableCollection;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

/**
 * Created by <Snack> on 23/02/2018.
 * It's distributed as part of Solar.
 */
public abstract class Baked implements IBakedModel {
	public abstract ImmutableCollection getTextures();
	public abstract Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites);
	public abstract VertexFormat getFormat();
	public abstract Baked applyFormat(VertexFormat format);
}
