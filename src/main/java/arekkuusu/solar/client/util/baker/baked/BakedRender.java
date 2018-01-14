/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.util.baker.baked;

import arekkuusu.solar.client.util.ResourceLibrary;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class BakedRender extends BakedPerspective {

	private Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms = ImmutableMap.copyOf(ITEM_TRANSFORMS);
	private ResourceLocation particle = ResourceLibrary.TRANSPARENT;

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
		return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(getParticle().toString());
	}

	public BakedRender setTransforms(Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
		this.transforms = ImmutableMap.copyOf(transforms);
		return this;
	}

	@Override
	public Map<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms() {
		return transforms;
	}

	public BakedRender setParticle(ResourceLocation particle) {
		this.particle = particle;
		return this;
	}

	public ResourceLocation getParticle() {
		return particle;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new ItemOverrideList(Collections.emptyList());
	}
}
