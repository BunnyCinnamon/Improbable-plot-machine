/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.common.block.BlockMonolithicGlyph;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class BakedMonolithicGlyph extends BakedBrightness {

	private TextureAtlasSprite[] overlay = new TextureAtlasSprite[16];
	private TextureAtlasSprite base;

	@Override
	public ImmutableCollection getTextures() {
		return new ImmutableList.Builder<>()
				.add(ResourceLibrary.MONOLITHIC_OVERLAY)
				.add(ResourceLibrary.MONOLITHIC)
				.build();
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		for(int i = 0; i < 16; i++) {
			this.overlay[i] = sprites.apply(ResourceLibrary.MONOLITHIC_OVERLAY[i]);
		}
		this.base = sprites.apply(ResourceLibrary.MONOLITHIC);
		return this;
	}

	@Override
	protected List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> quads = Lists.newArrayList();
		switch(MinecraftForgeClient.getRenderLayer()) {
			case SOLID:
				//Base
				quads.addAll(QuadBuilder.withFormat(format)
						.setFrom(0, 0, 0)
						.setTo(16, 16, 16)
						.addAll(base)
						.bake()
				);
				break;
			case CUTOUT_MIPPED:
				//Overlay
				if(state != null) {
					int glyph = state.getValue(BlockMonolithicGlyph.GLYPH);
					quads.addAll(QuadBuilder.withFormat(format)
							.setFrom(0, 0, 0)
							.setTo(16, 16, 16)
							.setHasBrightness(true)
							.addAll(overlay[glyph])
							.bake()
					);
				}
				break;
		}
		return quads;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
