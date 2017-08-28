/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class SchrodingerGlyphBakedModel extends BrightBakedModel {

	private final TextureAtlasSprite overlay;
	private final TextureAtlasSprite base;

	public SchrodingerGlyphBakedModel(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		this.overlay = getter.apply(ResourceLibrary.SCHRODINGER_GLYPH);
		this.base = getter.apply(ResourceLibrary.PRIMAL_STONE);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing facing, long rand) {
		if(state == null || facing != null) return Collections.emptyList();

		List<BakedQuad> quads = new ArrayList<>();

		quads.add(createQuad(new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 0.0D, 0.0D), this.base, false));
		quads.add(createQuad(new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(1.0D, 1.0D, 0.0D), this.base, false));
		quads.add(createQuad(new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 0.0D, 1.0D), this.base, false));
		quads.add(createQuad(new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), this.base, false));
		quads.add(createQuad(new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 0.0D, 0.0D), this.base, false));
		quads.add(createQuad(new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(1.0D, 0.0D, 1.0D), this.base, false));

		quads.add(createQuad(new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 0.0D, 0.0D), this.overlay, true));
		quads.add(createQuad(new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(1.0D, 1.0D, 0.0D), this.overlay, true));
		quads.add(createQuad(new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 0.0D, 1.0D), this.overlay, true));
		quads.add(createQuad(new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), this.overlay, true));
		quads.add(createQuad(new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 0.0D, 0.0D), this.overlay, true));
		quads.add(createQuad(new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(1.0D, 0.0D, 1.0D), this.overlay, true));

		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
