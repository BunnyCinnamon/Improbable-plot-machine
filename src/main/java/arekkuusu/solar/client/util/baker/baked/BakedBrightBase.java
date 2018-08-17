/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.client.util.baker.baked;

import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedBrightness;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/*
 * Created by <Arekkuusu> on 17/8/2018.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public abstract class BakedBrightBase extends BakedBrightness {

	final QuadCache cache = new QuadCache();

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing facing, long rand) {
		return facing != null ? Lists.newArrayList() : getQuads(state);
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

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(getParticle().toString());
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Lists.newArrayList());
	}
}
