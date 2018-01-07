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

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class BakedVacuumConveyor extends BakedBrightness {

	private final TextureAtlasSprite base;
	private final TextureAtlasSprite overlay_on;
	private final TextureAtlasSprite overlay_off;

	public BakedVacuumConveyor(VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> getter) {
		super(format);
		this.base = getter.apply(ResourceLibrary.VACUUM_CONVEYOR);
		this.overlay_on = getter.apply(ResourceLibrary.VACUUM_CONVEYOR_OVERLAY_ON);
		this.overlay_off = getter.apply(ResourceLibrary.VACUUM_CONVEYOR_OVERLAY_OFF);
	}

	@Override
	protected List<BakedQuad> getQuads(IBlockState state) {
		List<BakedQuad> quads = new ArrayList<>();
		//Base
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(3, 3, 3)
				.setTo(13, 13, 13)
				.addAll(0, 10, 0, 10, base)
				.bake()
		);
		//Overlay
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		quads.addAll(QuadBuilder.withFormat(format)
				.setFrom(3, 3, 3)
				.setTo(13, 13, 13)
				.addAll(0, 10, 0, 10, overlay_off)
				.addFace(facing, 0, 10, 0, 10, overlay_on)
				.bake()
		);
		return quads;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return base;
	}
}
