/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.BlockBaker;
import arekkuusu.solar.common.block.BlockQelaion;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.katsstuff.mirror.client.baked.QuadBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/**
 * Created by <Snack> on 24/02/2018.
 * It's distributed as part of Solar.
 */
public class BakedQelaion extends BakedBrightness {

	private TextureAtlasSprite base;
	private TextureAtlasSprite off;
	private TextureAtlasSprite on;

	@Override
	public ImmutableCollection getTextures() {
		return ImmutableList.of(
				ResourceLibrary.QELAION_BASE,
				ResourceLibrary.QELAION_INSIDE,
				ResourceLibrary.QELAION_ON,
				ResourceLibrary.QELAION_OFF
		);
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		this.base = sprites.apply(ResourceLibrary.QELAION_BASE);
		this.off = sprites.apply(ResourceLibrary.QELAION_OFF);
		this.on = sprites.apply(ResourceLibrary.QELAION_ON);
		return this;
	}

	@Override
	List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> list = Lists.newArrayList(BlockBaker.QELAION.getQuads()); //Sub model
		boolean hasNode = state != null && state.getValue(BlockQelaion.HAS_NODE);
		list.addAll(QuadBuilder.withFormat(format)
				.setFrom(5, 5, 5)
				.setTo(11, 11, 11)
				.addAll(0, 9, 0, 9, hasNode ? on : off)
				.setHasBrightness(true)
				.bakeJava()
		);
		return list;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
