/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
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
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class GravityHopperBakedModel extends BrightBakedModel {

	private final TextureAtlasSprite[] overlay = new TextureAtlasSprite[6];
	private final TextureAtlasSprite base;

	public GravityHopperBakedModel(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		for(int i = 0; i < 6; i++) {
			this.overlay[i] = getter.apply(ResourceLibrary.GRAVITY_HOPPER_GLYPH[i]);
		}

		this.base = getter.apply(ResourceLibrary.GRAVITY_HOPPER);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		EnumFacing face = state.getValue(BlockDirectional.FACING);

		List<BakedQuad> quads = new ArrayList<>();

		addCube(quads, base, base, base, base, base, base, false);
		switch(face) {
			case DOWN:
				addCube(quads, overlay[3], overlay[5], overlay[1], overlay[2], overlay[0], overlay[4], true);
				break;
			case UP:
				addCube(quads, overlay[5], overlay[3], overlay[1], overlay[2], overlay[0], overlay[4], true);
				break;
			case NORTH:
				addCube(quads, overlay[1], overlay[0], overlay[5], overlay[3], overlay[2], overlay[4], true);
				break;
			case SOUTH:
				addCube(quads, overlay[2], overlay[0], overlay[3], overlay[5], overlay[1], overlay[4], true);
				break;
			case EAST:
				addCube(quads, overlay[4], overlay[0], overlay[1], overlay[2], overlay[5], overlay[3], true);
				break;
			case WEST:
				addCube(quads, overlay[4], overlay[0], overlay[1], overlay[2], overlay[3], overlay[5], true);
				break;
		}

		return quads;
	}

	private void addCube(List<BakedQuad> quads, TextureAtlasSprite up, TextureAtlasSprite down, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite east, TextureAtlasSprite west, boolean shine) {
		quads.add(createQuad(new Vec3d(1.0D, 0.3125D, 0.0D), new Vec3d(1.0D, 0.3125D, 1.0D), new Vec3d(0.0D, 0.3125D, 1.0D), new Vec3d(0.0D, 0.3125D, 0.0D), down, shine));
		quads.add(createQuad(new Vec3d(0.0D, 0.6875D, 0.0D), new Vec3d(0.0D, 0.6875D, 1.0D), new Vec3d(1.0D, 0.6875D, 1.0D), new Vec3d(1.0D, 0.6875D, 0.0D), up, shine));
		quads.add(createQuad(new Vec3d(1.0D, 0.0D, 0.6875D), new Vec3d(1.0D, 1.0D, 0.6875D), new Vec3d(0.0D, 1.0D, 0.6875D), new Vec3d(0.0D, 0.0D, 0.6875D), south, shine));
		quads.add(createQuad(new Vec3d(0.0D, 0.0D, 0.3125D), new Vec3d(0.0D, 1.0D, 0.3125D), new Vec3d(1.0D, 1.0D, 0.3125D), new Vec3d(1.0D, 0.0D, 0.3125D), north, shine));
		quads.add(createQuad(new Vec3d(0.3125D, 0.0D, 1.0D), new Vec3d(0.3125D, 1.0D, 1.0D), new Vec3d(0.3125D, 1.0D, 0.0D), new Vec3d(0.3125D, 0.0D, 0.0D), west, shine));
		quads.add(createQuad(new Vec3d(0.6875D, 0.0D, 0.0D), new Vec3d(0.6875D, 1.0D, 0.0D), new Vec3d(0.6875D, 1.0D, 1.0D), new Vec3d(0.6875D, 0.0D, 1.0D), east, shine));
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
