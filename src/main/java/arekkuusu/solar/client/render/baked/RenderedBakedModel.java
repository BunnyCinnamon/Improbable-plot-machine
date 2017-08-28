/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class RenderedBakedModel implements IBakedModel {

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(ResourceLibrary.NOTHING.toString());
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Collections.emptyList());
	}
}
