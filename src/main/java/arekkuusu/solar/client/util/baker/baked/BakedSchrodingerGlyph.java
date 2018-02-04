/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
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
public class BakedSchrodingerGlyph extends BakedBrightness {

	private final TextureAtlasSprite overlay;
	private final TextureAtlasSprite base;

	public BakedSchrodingerGlyph(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		this.overlay = getter.apply(ResourceLibrary.SCHRODINGER_GLYPH);
		this.base = getter.apply(ResourceLibrary.PRIMAL_STONE);
	}

	@Override
	List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> quads = Lists.newArrayList();
		if(state == null) {
			//Base
			addBaseQuads(quads, format);
			//Overlay
			addOverlayQuads(quads, format, false);
		} else {
			switch(MinecraftForgeClient.getRenderLayer()) {
				case SOLID:
					//Base
					addBaseQuads(quads, format);
					break;
				case CUTOUT_MIPPED:
					//Overlay
					addOverlayQuads(quads, format, true);
					break;
			}
		}
		return quads;
	}

	private void addBaseQuads(List<BakedQuad> quads, VertexFormat format) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(0, 0, 0)
				.setTo(16, 16, 16)
				.addAll(base)
				.bake()
		);
	}

	private void addOverlayQuads(List<BakedQuad> quads, VertexFormat format, boolean bright) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(0, 0, 0)
				.setTo(16, 16, 16)
				.setHasBrightness(bright)
				.addAll(overlay)
				.bake()
		);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
