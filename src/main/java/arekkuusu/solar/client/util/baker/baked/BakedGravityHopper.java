/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.UP;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class BakedGravityHopper extends BakedBrightness {

	private final TextureAtlasSprite[] overlay = new TextureAtlasSprite[3];
	private final TextureAtlasSprite base;

	public BakedGravityHopper(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		for(int i = 0; i < 3; i++) {
			this.overlay[i] = getter.apply(ResourceLibrary.GRAVITY_HOPPER_OVERLAY[i]);
		}
		this.base = getter.apply(ResourceLibrary.GRAVITY_HOPPER);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		EnumFacing face = state.getValue(BlockDirectional.FACING);
		List<BakedQuad> quads = new ArrayList<>();
		switch(MinecraftForgeClient.getRenderLayer()) {
			case SOLID:
				//Base
				addCube(quads, face, base, base, base, false);
				break;
			case CUTOUT_MIPPED:
				//Overlay
				addCube(quads, face, overlay[0], overlay[1], overlay[2], true);
				break;
		}
		return quads;
	}

	private void addCube(List<BakedQuad> quads, EnumFacing facing, TextureAtlasSprite up, TextureAtlasSprite down, TextureAtlasSprite side, boolean shine) {
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(5, 5, 5)
				.setTo(11, 11, 11)
				.addAll(5F, 11F, 5F, 11F, side)
				.addFace(UP, 5F, 11F, 5F, 11F, up)
				.addFace(DOWN, 5F, 11F, 5F, 11F, down)
				.rotate(facing, EnumFacing.DOWN)
				.setHasBrightness(shine)
				.bake()
		);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
