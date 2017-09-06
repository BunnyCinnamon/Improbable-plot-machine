/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 05/09/2017.
 * It's distributed as part of Solar.
 */
public class BlinkerBakedModel extends BrightBakedModel {

	private final TextureAtlasSprite base;
	private final TextureAtlasSprite top;
	private final TextureAtlasSprite bottom;

	public BlinkerBakedModel(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		base = getter.apply(ResourceLibrary.BLINKER_BASE);
		top = getter.apply(ResourceLibrary.BLINKER_TOP);
		bottom = getter.apply(ResourceLibrary.BLINKER_BOTTOM);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> list = new ArrayList<>();
		EnumFacing facing = state.getValue(BlockDirectional.FACING);

		switch(facing) { //TODO: Add model
			case DOWN:
				break;
			case UP:
				break;
			case NORTH:
				break;
			case SOUTH:
				break;
			case WEST:
				break;
			case EAST:
				break;
		}

		return list;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
