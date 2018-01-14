/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.api.state.State;
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

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class BakedElectron extends BakedBrightness {

	private final TextureAtlasSprite base_on;
	private final TextureAtlasSprite base_off;

	public BakedElectron(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		this.base_on = getter.apply(ResourceLibrary.ELECTRON_ON);
		this.base_off = getter.apply(ResourceLibrary.ELECTRON_OFF);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();
		boolean on = state.getValue(State.POWER) > 0;
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(5.5D, 5.5D, 5.5D)
				.setTo(10.5D, 10.5D, 10.5D)
				.addAll(0F, 5F, 0, 5F, on ? base_on : base_off)
				.setHasBrightness(on)
				.bake()
		);
		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base_off;
	}
}
