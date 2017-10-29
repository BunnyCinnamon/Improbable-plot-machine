/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render.baked;

import arekkuusu.solar.api.helper.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by <Arekkuusu> on 03/08/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public abstract class BrightBakedModel implements IBakedModel {

	private static final Map<IBlockState, List<BakedQuad>> BAKED_CACHE = new HashMap<>();

	private final VertexFormat format;

	public BrightBakedModel(VertexFormat format) {
		this.format = new VertexFormat(format).addElement(DefaultVertexFormats.TEX_2S);
	}

	private void putVertex(UnpackedBakedQuad.Builder builder, Vector3 normal, double x, double y, double z, TextureAtlasSprite sprite, float u, float v, boolean hasBrightness) {
		for(int e = 0; e < format.getElementCount(); e++) {
			switch(format.getElement(e).getUsage()) {
				case POSITION:
					builder.put(e, (float) x, (float) y, (float) z, 1F);
					break;
				case COLOR:
					builder.put(e, 1F, 1F, 1F, 1F);
					break;
				case UV:
					if(format.getElement(e).getIndex() == 1) {
						if(hasBrightness) {
							builder.put(e, 1F, 1F);
						} else {
							builder.put(e);
						}
					} else {
						u = sprite.getInterpolatedU(u);
						v = sprite.getInterpolatedV(v);
						builder.put(e, u, v, 0F, 1F);
					}
					break;
				case NORMAL:
					if(hasBrightness) {
						builder.put(e, 0F, 1F, 0F);
					} else {
						builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z);
					}
					break;
				default:
					builder.put(e);
					break;
			}
		}
	}

	BakedQuad createQuad(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4, TextureAtlasSprite sprite, boolean hasBrightness) {
		return createQuad(v1, v2, v3, v4, sprite, 0F, 16F, 0F, 16F, hasBrightness);
	}

	BakedQuad createQuad(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4, TextureAtlasSprite sprite
			, float uMin, float uMax, float vMin, float vMax, boolean hasBrightness) {
		Vector3 normal = v3.copy().subtract(v2).cross(v1.copy().subtract(v2)).normalize();

		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		builder.setTexture(sprite);
		putVertex(builder, normal, v1.x, v1.y, v1.z, sprite, uMax, vMax, hasBrightness);
		putVertex(builder, normal, v2.x, v2.y, v2.z, sprite, uMax, vMin, hasBrightness);
		putVertex(builder, normal, v3.x, v3.y, v3.z, sprite, uMin, vMin, hasBrightness);
		putVertex(builder, normal, v4.x, v4.y, v4.z, sprite, uMin, vMax, hasBrightness);
		return builder.build();
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing facing, long rand) {
		if(state == null || facing != null) return Collections.emptyList();

		return BAKED_CACHE.computeIfAbsent(state, this::getQuads);
	}

	protected abstract List<BakedQuad> getQuads(IBlockState state);

	Vector3 vector(double x, double y, double z) {
		return new Vector3(x, y, z);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
}
