/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.api.state.State;
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

import static net.minecraft.util.EnumFacing.*;

/**
 * Created by <Arekkuusu> on 05/09/2017.
 * It's distributed as part of Solar.
 */
public class BakedBlinker extends BakedBrightness {

	private final TextureAtlasSprite base;
	private final TextureAtlasSprite top_on;
	private final TextureAtlasSprite bottom_on;
	private final TextureAtlasSprite top_off;
	private final TextureAtlasSprite bottom_off;

	public BakedBlinker(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		base = getter.apply(ResourceLibrary.BLINKER_BASE);
		top_on = getter.apply(ResourceLibrary.BLINKER_TOP_ON);
		bottom_on = getter.apply(ResourceLibrary.BLINKER_BOTTOM_ON);
		top_off = getter.apply(ResourceLibrary.BLINKER_TOP_OFF);
		bottom_off = getter.apply(ResourceLibrary.BLINKER_BOTTOM_OFF);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		boolean on = state.getValue(State.ACTIVE);
		//Base
		QuadBuilder base_quads = QuadBuilder.withFormat(format)
				.setFrom(2, 0, 2)
				.setTo(14, 1, 14)
				.addAll(2F, 14F, 2F, 2F, base)
				.addFace(UP, 2F, 14F, 3F, 14F, base)
				.addFace(DOWN, 2F, 14F, 2F, 14F, base);
		//Overlay
		QuadBuilder overlay_quads = QuadBuilder.withFormat(format)
				.setFrom(2, 0, 2)
				.setTo(14, 1, 14)
				.addAll(2F, 14F, 2F, 2F, on ? top_on : top_off)
				.addFace(UP, 2F, 14F, 2F, 14F, on ? top_on : top_off)
				.addFace(DOWN, 2F, 14F, 2F, 14F, on ? bottom_on : bottom_off)
				.setHasBrightness(true);
		switch(facing) {
			case DOWN:
				break;
			case UP:
				base_quads.rotate(Axis.X, 180F);
				overlay_quads.rotate(Axis.X, 180F);
				break;
			default:
				base_quads.rotate(Axis.X, 90F);
				base_quads.rotate(Axis.Y, -facing.getHorizontalAngle());
				base_quads.rotate(Axis.Y, -90F);
				overlay_quads.rotate(Axis.X, 90F);
				overlay_quads.rotate(Axis.Y, -facing.getHorizontalAngle());
				overlay_quads.rotate(Axis.Y, -90F);
		}
		quads.addAll(base_quads.bake());
		quads.addAll(overlay_quads.bake());
		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
