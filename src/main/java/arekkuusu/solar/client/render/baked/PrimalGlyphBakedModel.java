/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static arekkuusu.solar.api.state.Glyph.GLYPH;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class PrimalGlyphBakedModel extends BrightBakedModel {

	private final TextureAtlasSprite[] overlay = new TextureAtlasSprite[16];
	private final TextureAtlasSprite base;

	public PrimalGlyphBakedModel(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		for(int i = 0; i < 16; i++) {
			this.overlay[i] = getter.apply(ResourceLibrary.PRIMAL_GLYPH[i]);
		}

		this.base = getter.apply(ResourceLibrary.PRIMAL_STONE);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();

		quads.add(createQuad(vector(1.0D, 0.0D, 0.0D), vector(1.0D, 0.0D, 1.0D), vector(0.0D, 0.0D, 1.0D), vector(0.0D, 0.0D, 0.0D), this.base, false));
		quads.add(createQuad(vector(0.0D, 1.0D, 0.0D), vector(0.0D, 1.0D, 1.0D), vector(1.0D, 1.0D, 1.0D), vector(1.0D, 1.0D, 0.0D), this.base, false));
		quads.add(createQuad(vector(1.0D, 0.0D, 1.0D), vector(1.0D, 1.0D, 1.0D), vector(0.0D, 1.0D, 1.0D), vector(0.0D, 0.0D, 1.0D), this.base, false));
		quads.add(createQuad(vector(0.0D, 0.0D, 0.0D), vector(0.0D, 1.0D, 0.0D), vector(1.0D, 1.0D, 0.0D), vector(1.0D, 0.0D, 0.0D), this.base, false));
		quads.add(createQuad(vector(0.0D, 0.0D, 1.0D), vector(0.0D, 1.0D, 1.0D), vector(0.0D, 1.0D, 0.0D), vector(0.0D, 0.0D, 0.0D), this.base, false));
		quads.add(createQuad(vector(1.0D, 0.0D, 0.0D), vector(1.0D, 1.0D, 0.0D), vector(1.0D, 1.0D, 1.0D), vector(1.0D, 0.0D, 1.0D), this.base, false));

		int glyph = state.getValue(GLYPH).ordinal();

		quads.add(createQuad(vector(1.0D, 0.0D, 0.0D), vector(1.0D, 0.0D, 1.0D), vector(0.0D, 0.0D, 1.0D), vector(0.0D, 0.0D, 0.0D), this.overlay[glyph], true));
		quads.add(createQuad(vector(0.0D, 1.0D, 0.0D), vector(0.0D, 1.0D, 1.0D), vector(1.0D, 1.0D, 1.0D), vector(1.0D, 1.0D, 0.0D), this.overlay[glyph], true));
		quads.add(createQuad(vector(1.0D, 0.0D, 1.0D), vector(1.0D, 1.0D, 1.0D), vector(0.0D, 1.0D, 1.0D), vector(0.0D, 0.0D, 1.0D), this.overlay[glyph], true));
		quads.add(createQuad(vector(0.0D, 0.0D, 0.0D), vector(0.0D, 1.0D, 0.0D), vector(1.0D, 1.0D, 0.0D), vector(1.0D, 0.0D, 0.0D), this.overlay[glyph], true));
		quads.add(createQuad(vector(0.0D, 0.0D, 1.0D), vector(0.0D, 1.0D, 1.0D), vector(0.0D, 1.0D, 0.0D), vector(0.0D, 0.0D, 0.0D), this.overlay[glyph], true));
		quads.add(createQuad(vector(1.0D, 0.0D, 0.0D), vector(1.0D, 1.0D, 0.0D), vector(1.0D, 1.0D, 1.0D), vector(1.0D, 0.0D, 1.0D), this.overlay[glyph], true));

		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
