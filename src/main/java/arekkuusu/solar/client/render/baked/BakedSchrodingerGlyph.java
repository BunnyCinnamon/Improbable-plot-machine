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
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
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
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();
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
				quads.addAll(QuadBuilder.withFormat(format)
						.setFrom(0, 0, 0)
						.setTo(16, 16, 16)
						.setHasBrightness(true)
						.addAll(overlay)
						.bake()
				);
				break;
		}
		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
