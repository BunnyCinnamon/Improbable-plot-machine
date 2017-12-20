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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.minecraft.util.EnumFacing.*;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class BakedGravityHopper extends BakedBrightness {

	private final TextureAtlasSprite[] overlay = new TextureAtlasSprite[6];
	private final TextureAtlasSprite base;

	public BakedGravityHopper(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		for(int i = 0; i < 6; i++) {
			this.overlay[i] = getter.apply(ResourceLibrary.GRAVITY_HOPPER_OVERLAY[i]);
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
			case WEST:
				addCube(quads, overlay[4], overlay[0], overlay[1], overlay[2], overlay[3], overlay[5], true);
				break;
			case EAST:
				addCube(quads, overlay[4], overlay[0], overlay[1], overlay[2], overlay[5], overlay[3], true);
				break;
		}
		return quads;
	}

	private void addCube(List<BakedQuad> quads, TextureAtlasSprite up, TextureAtlasSprite down, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite east, TextureAtlasSprite west, boolean shine) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(5, 5, 5)
				.setTo(11, 11, 11)
				.addFace(UP, 5F, 11F, 5F, 11F, up)
				.addFace(DOWN, 5F, 11F, 5F, 11F, down)
				.addFace(NORTH, 5F, 11F, 5F, 11F, north)
				.addFace(SOUTH, 5F, 11F, 5F, 11F, south)
				.addFace(EAST, 5F, 11F, 5F, 11F, east)
				.addFace(WEST, 5F, 11F, 5F, 11F, west)
				.setHasBrightness(shine)
				.bake()
		);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
