/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 05/09/2017.
 * It's distributed as part of Solar.
 */
public class BlinkerBakedModel extends BrightBakedModel {

	private final TextureAtlasSprite base;
	private final TextureAtlasSprite top_on;
	private final TextureAtlasSprite bottom_on;
	private final TextureAtlasSprite top_off;
	private final TextureAtlasSprite bottom_off;

	public BlinkerBakedModel(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		base = getter.apply(ResourceLibrary.BLINKER_BASE);
		top_on = getter.apply(ResourceLibrary.BLINKER_TOP_ON);
		bottom_on = getter.apply(ResourceLibrary.BLINKER_BOTTOM_ON);
		top_off = getter.apply(ResourceLibrary.BLINKER_TOP_OFF);
		bottom_off = getter.apply(ResourceLibrary.BLINKER_BOTTOM_OFF);
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing facing, long rand) {
		if(state == null || facing != null) return Collections.emptyList();

		return getQuads(state);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		boolean on = state.getValue(State.ACTIVE);

		switch(facing) {
			case DOWN:
				facingUp(quads, on);
				break;
			case UP:
				facingDown(quads, on);
				break;
			default:
				facingWall(quads, facing, on);
		}

		return quads;
	}

	private void facingUp(List<BakedQuad> quads, boolean on) {
		//Back
		addLayers(quads, vector(0, 0, 0), vector(1, 0, 0), vector(1, 0, 1), vector(0, 0, 1)
				, on ? bottom_on : bottom_off, 0F, 16F, 0F, 16F);
		//Front
		addLayers(quads, vector(0, 0.0625D, 0), vector(0, 0.0625D, 1), vector(1, 0.0625D, 1), vector(1, 0.0625D, 0)
				, on ? top_on : top_off, 0F, 16F, 0F, 16F);
		//Sides
		addHorizontalFaces(quads, false, on);
	}

	private void facingDown(List<BakedQuad> quads, boolean on) {
		//Back
		addLayers(quads, vector(0, 1, 0), vector(0, 1, 1), vector(1, 1, 1), vector(1, 1, 0)
				, on ? bottom_on : bottom_off, 0F, 16F, 0F, 16F);
		//Front
		addLayers(quads, vector(0, 0.9375D, 0), vector(1, 0.9375D, 0), vector(1, 0.9375D, 1), vector(0, 0.9375D, 1)
				, on ? top_on : top_off, 0F, 16F, 0F, 16F);
		//Sides
		addHorizontalFaces(quads, true, on);
	}

	private void addHorizontalFaces(List<BakedQuad> quads, boolean upsideDown, boolean on) {
		float yMax = upsideDown ? 1F : 0.0625F;
		float yMin = upsideDown ? 0.9375F : 0F;
		Vector3[] vectors = {vector(0, yMin, 0.125D), vector(0, yMax, 0.125D), vector(1, yMax, 0.125D), vector(1, yMin, 0.125D)};
		for(float angle = 0; angle < 270F; angle += 67.544F) {
			Vector3[] rotated = new Vector3[4];
			for(int vec = 0; vec < 4; vec++) {
				rotated[vec] = rotateVecYaw(vectors[vec].copy(), angle);
			}

			//Side
			addLayers(quads, rotated[0], rotated[1], rotated[2], rotated[3]
					, on ? top_on : top_off, 0, 16F, 2F, 3F);
		}
	}

	private void facingWall(List<BakedQuad> quads, EnumFacing facing, boolean on) {
		float angle = facing.getHorizontalAngle();
		if(angle > 0) {
			angle -= 22.456F * (float) ((int) (angle / 90F));
		}
		//Back
		addLayers(quads, rotateVecYaw(vector(0,0,1), angle), rotateVecYaw(vector(1, 0, 1), angle), rotateVecYaw(vector(1, 1, 1), angle), rotateVecYaw(vector(0, 1, 1), angle)
				, on ? bottom_on : bottom_off, 0F, 16F, 0F, 16F);
		//Front
		addLayers(quads, rotateVecYaw(vector(0,0,0.9375D), angle), rotateVecYaw(vector(0, 1, 0.9375D), angle), rotateVecYaw(vector(1, 1, 0.9375D), angle), rotateVecYaw(vector(1, 0, 0.9375D), angle)
				, on ? top_on : top_off, 0F, 16F, 0F, 16F);
		//Sides
		Vector3[] vectors = {vector(0.125D,0,0.9375D), vector(0.125D,0,1), vector(0.125D,1,1), vector(0.125D, 1, 0.9375D)};
		for(float pitch = 0; pitch < 270; pitch += 67.544F) {
			Vector3[] rotated = new Vector3[4];
			for(int vec = 0; vec < 4; vec++) {
				Vector3 other = rotateVecPitch(vectors[vec].copy(), pitch);
				rotated[vec] = rotateVecYaw(other, angle);
			}

			//Side
			addLayers(quads, rotated[0], rotated[1], rotated[2], rotated[3]
					, on ? top_on : top_off, 0, 16F, 2F, 3F);
		}
	}

	private Vector3 rotateVecYaw(Vector3 vec, float angle) {
		return vec.subtract(0.5D, 0, 0.5D).rotateYaw(angle).add(0.5D, 0, 0.5D);
	}

	private Vector3 rotateVecPitch(Vector3 vec, float angle) {
		return vec.subtract(0.5D, 0.5D, 0).rotatePitchX(angle).add(0.5D, 0.5D, 0);
	}

	private void addLayers(List<BakedQuad> quads, Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4, TextureAtlasSprite sprite, float uMin, float uMax, float vMin, float vMax) {
		quads.add(createQuad(v1, v2, v3, v4, base, uMin, uMax, vMin, vMax, false));
		quads.add(createQuad(v1, v2, v3, v4, sprite, uMin, uMax, vMin, vMax, true));
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
