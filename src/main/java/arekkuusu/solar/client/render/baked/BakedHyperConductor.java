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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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
public class BakedHyperConductor extends BakedBrightness {

	private final TextureAtlasSprite overlay;
	private final TextureAtlasSprite base;

	public BakedHyperConductor(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		this.base = getter.apply(ResourceLibrary.HYPER_CONDUCTOR);
		this.overlay = getter.apply(ResourceLibrary.HYPER_CONDUCTOR_OVERLAY);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();
		//Base
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(0, 0, 0)
				.setTo(16, 16, 16)
				.addAll(base)
				.bake()
		);
		//Overlay
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(0, 0, 0)
				.setTo(16, 16, 16)
				.setHasBrightness(true)
				.addAll(overlay)
				.bake()
		);
		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
