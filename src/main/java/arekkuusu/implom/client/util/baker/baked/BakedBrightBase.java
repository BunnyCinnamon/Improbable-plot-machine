/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client.util.baker.baked;

import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.mirror.client.baked.Baked;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedBrightness;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.function.Function;

/*
 * Created by <Arekkuusu> on 17/8/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public abstract class BakedBrightBase extends BakedBrightness {

	private final TextureAtlasSprite particle;
	final QuadCache quadCache = new QuadCache();

	public BakedBrightBase(VertexFormat format, TextureAtlasSprite particle) {
		applyFormat(format);
		this.particle = particle;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing facing, long rand) {
		return facing != null ? Lists.newArrayList() : getQuads(state);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return particle;
	}

	//TODO: REMOVE ME!
	@Override
	public Baked applyTextures(Function<ResourceLocation, TextureAtlasSprite> sprites) {
		return this;
	}

	//TODO: REMOVE ME!
	@Override
	public ResourceLocation[] getTextures() {
		return new ResourceLocation[]{};
	}

	//TODO: REMOVE ME!
	@Override
	public ResourceLocation getParticle() {
		return null;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}
}
