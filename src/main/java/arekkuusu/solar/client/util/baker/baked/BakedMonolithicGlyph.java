/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.common.block.BlockMonolithicGlyph;
import com.google.common.collect.Lists;
import net.katsstuff.mirror.client.baked.Baked;
import net.katsstuff.mirror.client.baked.BakedBrightness;
import net.katsstuff.mirror.client.baked.QuadBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * This class was created by <Arekkuusu> on 25/06/2017.
 * It's distributed as part of Solar under
 * the MIT license.
 */
@SideOnly(Side.CLIENT)
public class BakedMonolithicGlyph extends BakedBrightness {

	private TextureAtlasSprite[] overlay = new TextureAtlasSprite[16];
	private TextureAtlasSprite base;

	@Override
	public ResourceLocation[] getTextures() {
		ResourceLocation[] textures = Arrays.copyOf(ResourceLibrary.MONOLITHIC_OVERLAY, 17);
		textures[16] = ResourceLibrary.MONOLITHIC;
		return textures;
	}

	@Override
	public ResourceLocation getParticle() {
		return ResourceLibrary.MONOLITHIC;
	}

	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		for(int i = 0; i < 16; i++) {
			this.overlay[i] = sprites.apply(ResourceLibrary.MONOLITHIC_OVERLAY[i]);
		}
		this.base = sprites.apply(ResourceLibrary.MONOLITHIC);
		return this;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, VertexFormat format) {
		List<BakedQuad> quads = Lists.newArrayList();
		switch(MinecraftForgeClient.getRenderLayer()) {
			case SOLID:
				//Base
				quads.addAll(QuadBuilder.withFormat(format)
						.setFrom(0, 0, 0)
						.setTo(16, 16, 16)
						.addAll(base)
						.bakeJava()
				);
				break;
			case CUTOUT_MIPPED:
				//Overlay
				if(state != null) {
					int glyph = state.getValue(BlockMonolithicGlyph.GLYPH);
					quads.addAll(QuadBuilder.withFormat(format)
							.setFrom(0, 0, 0)
							.setTo(16, 16, 16)
							.setHasBrightness(true)
							.addAll(overlay[glyph])
							.bakeJava()
					);
				}
				break;
		}
		return quads;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
}
